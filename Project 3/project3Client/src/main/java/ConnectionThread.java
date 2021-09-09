import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.Scanner;


public class ConnectionThread extends Thread{
	
	private Socket socketClient;  // store the socket connection with the server
	private ObjectInputStream in; // store the input stream of connection with server
	private ObjectOutputStream out; // store the output stream of connection with server
	private int betAmount;  // store the bet amount
	private String betPlace; // store the result the client placed a bet on
	private String ip;
	private int portNum;
	
	private Consumer<BaccaratInfo> callback;
	private Consumer<Boolean> start;

	
	 ConnectionThread(String ipAddress, int portNumber, Consumer<BaccaratInfo> call, Consumer<Boolean> startGame) {
		
		callback = call;
		ip = ipAddress;
		portNum = portNumber;
		start = startGame;
	}
	 
	 /* This method is used by the client GUI to set the bet amount and bet choice of the client so it can be used when 
	 	sending baccaratInfo object to the server.
	 */
	 
	public void setInfo(int bet, String betChoice) {
		this.betAmount = bet;
		this.betPlace = betChoice;
	}
	
	// used by client GUI to send BaccaratInfo object to the server

	public void send() {
		
		try {		
			BaccaratInfo data = new BaccaratInfo();  
			data.setBetPlace(betPlace);
			data.setBetAmount(betAmount);
			out.writeObject(data);  // send data to the server
			out.reset();
		} catch (IOException e) {
			System.out.println("Disconnected from the server");
		}
	}
	
	public void run() {
		
		
		try {
			socketClient =  new Socket(ip,portNum);
		    out = new ObjectOutputStream(socketClient.getOutputStream()); // get output stream of the connection
		    in = new ObjectInputStream(socketClient.getInputStream());   // get input stream of the connection
		    socketClient.setTcpNoDelay(true);
		    
		    start.accept(true);
		}
		catch(Exception e) {
		}

		
		while(true) {
			 
			try {
				BaccaratInfo info = (BaccaratInfo) in.readObject();  // read BaccaratInfo object from server
				
				callback.accept(info); // send the info so it can be displayed to the client
			}
			catch(Exception e) {}
		}
	
    }

}
