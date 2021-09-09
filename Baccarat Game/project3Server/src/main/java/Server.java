import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

// This class will communicate with the clients constantly through sockets and also accept new clients.

public class Server{

	int count = 1;	// store the number of clients that have been on the server
	ArrayList<ClientThread> clients;   // store all the ClientThread objects for each client
	TheServer server;  // Store the TheServer
	
	private Consumer<Integer> addClient;   // this will be called to update the server GUI when client joins
	private Consumer<Integer> removeClient;  // this will be called to update the server GUI when client leaves
	private Consumer<BaccaratInfo> gameData; // this will display the results of a game on theh GUI
	private Consumer<String> send; // this will display message on server GUI when client joins or leaves the server or  when client plays a  hand
	
	private boolean turnOff;  // when server is turned off
	private int portNumber;
	
	
	Server( int portNum, Consumer<Integer> add, Consumer<Integer> remove, Consumer<BaccaratInfo> info,
			Consumer<String> clientChange){
	
		// set all variables that store Consumer objects to work with the server GUI
	
		this.addClient = add;
		this.removeClient = remove;
		this.gameData = info;
		this.send = clientChange;
		
		portNumber = portNum;
		clients = new ArrayList<ClientThread>();    
		server = new TheServer();
		server.start();	// start the thread to begin accepting new clients
		
		turnOff = false;
	}
	
	
	/* This class extends thread, and it will accept new clients, it will also start another thread 
	   of class ClientThread so the server can communicate with that client, and add instance of ClientThread to array-list clients.
	*/
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(portNumber);){
		  
				while(true) {

					ClientThread c = new ClientThread(mysocket.accept(), count); // accept new client and create instance of ClientThread
					clients.add(c);		// add an instance of ClientThread to clients
					addClient.accept(1);  // update server GUI to change number of clients label
					send.accept(("Client " + Integer.toString(count) + " has joined the server!")); // update server GUI to display a client joined
					count++;  // increase number of clients joined so far
					c.start(); // start the client thread 
				
			    }
			}//end of try
				catch(Exception e) {
				}
			}//end of while
	}
	
	
	/* This class will communicate with the client using sockets and it will receive a BaccaratInfo object, use it to simulate
		a Baccarat Game, and then pass the details of the completed game to BaccaratInfo object and send that object to client.
	*/

	class ClientThread extends Thread{
	
		Socket connection;  // socket connection with the client
		int count;   // the number in the order the client joined the server, 1 being the first client to join the server
		ObjectInputStream in;   // input stream of socket
		ObjectOutputStream out; // output stream of socket
		boolean flag;  // used to differentiate if the client is playing his first game or the games following the first
		
		ClientThread(Socket s, int count){
			this.connection = s; 
			this.count = count;	
			flag = false;
		}
			
		/* Given a BaccaratInfo object, this method will simulate a BaccaratGame using the information in BaccaratInfo Object,
			and then it will store the result of the game to the BaccaratInfo Object and send that object back to the user.
		*/
		
		public void getGame(BaccaratInfo info) {
			 BaccaratGame game = new BaccaratGame(); // create instance of BaccaratGame
			 game.currentBet = info.getBetAmount(); //set the get bet amount of the user
			 		 
			 int amount = (int) game.evaluateWinnings();  // get winnings of the user
			 
			 if(game.result.equals(info.getBetPlace())) { // if user bet correctly on the result
				 info.setWon(amount);
			 }else {
				 info.setWon((-1 * info.getBetAmount()));
			 }
			 
			 

			 ArrayList<Card> playerHand = game.playerHand; // get player cards
			 ArrayList<Card> bankerHand = game.bankerHand; // get banker cards
			 
			 // add player and banker cards to info
			 
			 for(int i = 0; i < playerHand.size(); i++){
				 info.addPlayerCard(playerHand.get(i).getValue(), playerHand.get(i).getSuite());
			 }
			 
			 for(int i = 0; i < bankerHand.size(); i++) {
				 info.addBankerCard(bankerHand.get(i).getValue(), bankerHand.get(i).getSuite());
			 }
			 
			 info.setNumOfPlayerCards(playerHand.size());  // set the size of player cards
			 info.setNumOfBankerCards(bankerHand.size()); // set the size of banker cards
			 
			 info.setResult(game.result);  // set the result of the game
			 
			 info.setPlayerScore(BaccaratGameLogic.handTotal(playerHand)); // set the total points of the player
			 info.setBankerScore(BaccaratGameLogic.handTotal(bankerHand)); // set the total points of the banker
			 
			 info.client = count;
			 
			   
		}
	
		
		public void run(){
			
			try {
				in = new ObjectInputStream(connection.getInputStream()); // get the input stream of the socket
				out = new ObjectOutputStream(connection.getOutputStream()); // get the output stream of the socket
				connection.setTcpNoDelay(true);	
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}
				
			 while(true) {
				 
				   try {
					   
					   BaccaratInfo info = (BaccaratInfo)in.readObject(); // read in BaccaratInfo object from the client
					   
					   if(flag) {  // client is playing his first hand
						   send.accept("Client " + Integer.toString(count) + " is playing another hand!");
					   }else {  // client is playing another hand
						   send.accept("Client " + Integer.toString(count) + " is playing a hand!");

					   }
					   
					   getGame(info);  //  simulates a round of  BaccaratGame  and store it into info
					   gameData.accept(info); // send info to server GUI to display the result of the game
					   out.writeObject(info); // send info to client 
					   out.reset();  // reset out stream
					   
					    
				   }catch(Exception e) { // client left the server
					   
					   if(turnOff) {  // server was turned off
						   return;
					   }
				    	
				    	clients.remove(this);  // remove ClientThread instance of the client  from the array-list clients
				    	send.accept("Client " + Integer.toString(count) + " left the server!"); // send message to server GUI that client left
				    	removeClient.accept(1); // update the number of clients label in server GUI
				    	break;
				  }
				   
				   flag = true; // client played his first hand
			 }
		}//end of run
		
		
	}//end of client thread
	
	// This method when called will close all the connections with each connected client
	
	void stopServer() {
		turnOff = true;
		
		for(int i = 0; i < clients.size(); i++) {
			try {
				clients.get(i).connection.close();  // close connection
			} catch (IOException e) {
			}
		}
		
	}
		
}