import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class BaccaratGUIServer extends Application {
	

	private int portNumber;
	private ListView<Text> listItems;
	private Label numberClientsLabel;
	private int numClients;
	private ListView<String> listItems2;
	private Stage stage;
	private Server server;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		primaryStage.setTitle("Baccarat Server");	
		primaryStage.setScene(createStartScene());
		stage = primaryStage;
		primaryStage.show(); 
	}
	
	private Scene createServerControl() {
		BorderPane pane = new BorderPane();
		
		// create a Button to stop server
		
		Button stopServer = new Button("Stop Server");
		stopServer.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #D3D3D3;"  + "-fx-font-size : 20px");
		stopServer.setPrefSize(150, 60);
		stopServer.setOnAction(e->{
			server.stopServer(); // close connections with the client
			Platform.exit();
		});
		
	
		// create listview, this will display the results of the games the client play
		
		listItems = new ListView<Text>();	
		listItems.setStyle("-fx-font-family : Helvetica Neue;");
		listItems.setPrefSize(300, 300);
		listItems.setMaxSize(300, 300);
	
		// this listview will display the status of each client when it changes, so if client joins,leaves, or plays another hand

		listItems2 = new ListView<String>();
		listItems2.setStyle("-fx-font-family : Helvetica Neue;");
		listItems2.setPrefSize(270, 180);
		listItems2.setMaxSize(270, 180);
		
		// label to display the number of active clients
		
		numberClientsLabel = new Label("Number of Clients : 0");
		numberClientsLabel.setStyle("-fx-font-family : Helvetica Neue;");
		numberClientsLabel.setFont(new Font(20));
		numClients = 0;
		
		Label gameDetails = new Label("Results of Games");  // the list view beneath the label will display details of games played by clients
		gameDetails.setStyle("-fx-font-family : Helvetica Neue;");
		gameDetails.setFont(new Font(20));
		
		Label clientEvents = new Label("Client Events"); // the list view beneath the label will display events of clients like joins or leaves  server
		clientEvents.setStyle("-fx-font-family : Helvetica Neue;");
		clientEvents.setFont(new Font(20));

		
		VBox infoBox = new VBox(numberClientsLabel, gameDetails ,listItems, clientEvents ,listItems2, stopServer);
		infoBox.setSpacing(30);
		pane.setCenter(infoBox);
		infoBox.setAlignment(Pos.CENTER);
		
		pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		return new Scene(pane, 800,800);
	}
	
	
	private Scene createStartScene() {
		BorderPane pane = new BorderPane();
		
		Label serverLabel = new Label("Server");
		serverLabel.setStyle("-fx-font-family : Helvetica Neue;");
		serverLabel.setFont(new Font(35));
		serverLabel.setAlignment(Pos.CENTER);
		HBox hbox = new HBox(serverLabel);
		hbox.setAlignment(Pos.TOP_CENTER);
		pane.setTop(hbox);
		
		TextField portField = new TextField();  // field to enter port Number
		portField.setStyle("-fx-font-family : Helvetica Neue;");
		
		Button startServer = new Button("Start");   // button to start the server
		startServer.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #D3D3D3;"  + "-fx-font-size : 20px");
		
		startServer.setOnAction(new EventHandler<ActionEvent>() { 

			@Override
			public void handle(ActionEvent event) {         
			
				startServer.setDisable(true);	
				 server = new Server(  portNumber,
					x->{
						Platform.runLater(() -> {    // pass in the action to update client label for the GUI when client joins
							numClients += 1;
							numberClientsLabel.setText("Number of Clients : " + Integer.toString(numClients));
						});
					},
					
					y->{
						Platform.runLater(() -> { // pass in the action to update client label for GUI when client leaves
							numClients -= 1;
							numberClientsLabel.setText("Number of Clients : " + Integer.toString(numClients));
						});
					},
					
					data->{  // Update the list-view to display the result of a new round played by the client
						Platform.runLater(() -> {
						
							int playerpoints = data.getPlayerScore();  
							int bankerpoints = data.getBankerScore();
					
							// Create strings to display results and format them
							
							String client = "Client " + Integer.toString(data.client) + "'s game results : \n";
							String points = "Player Total : " + Integer.toString(playerpoints) + " Banker Total : " + Integer.toString(bankerpoints) + " \n";
							String result = "Result : " + ((data.getResult() != "Draw") ? data.getResult() + " Wins"  + " \n" : ("Draw" + " \n")) ;
							String clientBet = "Client Bet : " + Integer.toString(data.getBetAmount()) + " \n";
							String clientBetChoice = "Client Bet Choice : " + data.getBetPlace() + " \n";
							
							String a = "won";
							if(data.getWon() < 0) {
								a = "lost";
							}
							
							String won = "Client " + a + " : " + Integer.toString(data.getWon());
							String all = client + points + result + clientBet + clientBetChoice + won;
							
							Text text = new Text(all);
							text.setStyle("-fx-font-family : Helvetica Neue;");
							listItems.getItems().add(text);	// add	the result of the game to display it
							listItems.scrollTo(listItems.getItems().size() - 1);
						
						});
					},
					
					message -> {    // Update ListItems 2 to display the newly send message
						Platform.runLater(() -> {
							listItems2.getItems().add(message);
							listItems2.scrollTo(listItems2.getItems().size() - 1);
						});
					}			   
				);
				
				stage.setScene(createServerControl());  // change scene to display game information
				
			}		
		});
			
		startServer.setPrefSize(100, 70);
		startServer.setDisable(true);
		
		portField.setAlignment(Pos.CENTER);
		portField.setPrefSize(100, 50);
		portField.setMaxSize(100, 50);
		portField.setPromptText("Port");
		
		Label portLabel = new Label("Enter Port Number");
		portLabel.setStyle("-fx-font-family : Helvetica Neue;");
		portLabel.setFont(new Font(20));
		portLabel.setAlignment(Pos.CENTER);
		
		Button portButton = new Button("Set Port"); // button to set port once the port number is typed into portField
		portButton.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #D3D3D3;");
		HBox portBox = new HBox(portField, portButton);
		portBox.setSpacing(20);
		portBox.setAlignment(Pos.CENTER);
		portBox.setPadding(new Insets(0,-30,50,0));
		portButton.setOnAction(new EventHandler<ActionEvent> () {  // portButton was clicked, so set port number

			@Override
			public void handle(ActionEvent event) {
				String regex = "\\d{4}";
				String regex2 = "\\d{5}";
				
				boolean a = portField.getText().matches(regex); // check if number is a correct port number 
				boolean b = portField.getText().matches(regex2); // check if number is a correct port number
				
				if(a || b) {  // if the entered string is a 4 or 5 digit number, then its a port number
					portNumber = Integer.parseInt(portField.getText());  // the port number
					startServer.setDisable(false);  // enable start server button
				}else {
					startServer.setDisable(true);  // disable start server button since port number is invalid
				}
	
			}
			
		});

		
		VBox vbox = new VBox(portLabel,portBox,startServer);
		pane.setCenter(vbox);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(20);

		pane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

		
		return new Scene(pane, 800, 800);
	}

}






