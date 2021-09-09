import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BaccaratClient extends Application {
	
	private String betChoice; // the result the user is betting on either player win or banker win or draw
	
	private ArrayList<ImageView> playerCards;  // store images of player cards
	private ArrayList<ImageView> bankerCards;  // store images of banker cards
	
	private int betAmount;  // the amount bet by the user
	
	private ConnectionThread connection;  // store the thread class which communicates with the server
	
	private int playerTotal; // total score of the player
	private int bankerTotal; // total score of the banker

	private Label bankerPointsLabel;  // label to display banker points
	private Label playerPointsLabel; // label to display player points
	
	private Label resultLabel;  // label to display result
	private Label resultDescription; // label to display result description
	
	private Label totalWinningsLabel; // label to display total winnings
	private int totalWinnings;  // label to store total winnings
	
	private int i;  // used as a counter to animate drawn player and banker cards
	
	private int portNumber;  // store port number
	private boolean portFlag;
	
	private String IpAddress;  // store ip address
	private boolean ipFlag;
	
	private Button connectButton;  // used to connect to the server
	private Stage stage;
	
	private Button playButton;  // play round
	private Button nextButton; // transition to the next round
	
	private boolean animationMode;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		betChoice = "";
		betAmount = - 1;
		primaryStage.setTitle("Baccarat Game");	
		stage = primaryStage;
		Scene scene = createStartScene();
		primaryStage.setScene(scene);
		primaryStage.show(); 
		animationMode = false;
	}
	
	public Scene createStartScene() {
		BorderPane pane = new BorderPane();
	
		Label gameLabel = new Label("Baccarat Game");
		gameLabel.setStyle("-fx-font-family : Helvetica Neue;");
		gameLabel.setFont(new Font(30));
		HBox gameLabelBox = new HBox(gameLabel);
		gameLabelBox.setAlignment(Pos.TOP_CENTER);
		pane.setTop(gameLabelBox);
		
		Label enterLabel = new Label("Enter IP Address And Port Number");
		enterLabel.setStyle("-fx-font-family : Helvetica Neue;");
		enterLabel.setFont(new Font(20));
		
		
		TextField ipField = new TextField();
		ipField.setStyle("-fx-font-family : Helvetica Neue;");
		TextField portField = new TextField();
		portField.setStyle("-fx-font-family : Helvetica Neue;");
		ipField.setPromptText("IP Address");
		portField.setPromptText("Port Number");
		
		ipField.setPrefSize(130, 50);
		ipField.setMaxSize(130, 50);
		portField.setPrefSize(130, 50);
		portField.setMaxSize(130, 50);
		
		Button ipButton = new Button("Set IP Address");
		ipButton.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #7FFFD4;");
		ipButton.setOnAction(e->{
			String IPADDRESS_PATTERN =  "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
			boolean a = ipField.getText().matches(IPADDRESS_PATTERN);
			if(a) {  // if valid IP address
				IpAddress = ipField.getText();
				ipFlag = true;
				
				if(portFlag) {
					connectButton.setDisable(false);  // enable start server button
				}
				
			}else {
				connectButton.setDisable(true);  // disable start server button since port number is invalid
				ipFlag = false;
			}
		});
		
		HBox ipBox = new HBox(ipField, ipButton);
		ipBox.setAlignment(Pos.CENTER);
		ipBox.setSpacing(25);
		ipBox.setPadding(new Insets(0,-65,0,75));
		
		Button portButton = new Button("Set Port");
		portButton.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #7FFFD4;");
		//portButton.setPadding(new Insets(10,0,0,0));
		HBox portBox = new HBox(portField, portButton);
		portBox.setPrefSize(100, 100);
		portBox.setSpacing(25);
		portBox.setAlignment(Pos.CENTER);
		portBox.setPadding(new Insets(0,-50,0,50));
		portButton.setOnAction(new EventHandler<ActionEvent> () {  // portButton was clicked, so set port number

			@Override
			public void handle(ActionEvent event) {
				String regex = "\\d{4}";
				String regex2 = "\\d{5}";
				
				boolean a = portField.getText().matches(regex); // check if number is a correct port number 
				boolean b = portField.getText().matches(regex2); // check if number is a correct port number
				
				if(a || b) {  // if the entered string is a 4 or 5 digit number, then its a port number
					portNumber = Integer.parseInt(portField.getText());  // the port number
					portFlag = true;
					
					if(ipFlag) {
						connectButton.setDisable(false);  // enable start server button
					}
					
				}else {
					connectButton.setDisable(true);  // disable start server button since port number is invalid
					portFlag = false;
				}
	
			}
			
		});
		
		connectButton = new Button("Connect");
		connectButton.setDisable(true);
		connectButton.setPrefSize(100, 50);
		connectButton.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color: #7FFFD4;");
		connectButton.setOnAction(e->{			
					connection = new ConnectionThread( IpAddress, portNumber,
							x -> {
								Platform.runLater(() -> {    // pass in the action to update client GUI  
									animationMode = true;
									setImages(x);  // animate drawing of player and banker cards
								}); 
							}, 
							
							b -> {
								Platform.runLater(() -> {    // pass in the action to update client GUI  
									if(b) {
										stage.setScene(createGameScene());
									}
								});
							}
					);
					connection.start();
		});

		VBox fieldBox = new VBox(enterLabel, ipBox, portBox);
		fieldBox.setSpacing(35);
		
		fieldBox.getChildren().add(connectButton);
		
		pane.setCenter(fieldBox);
		fieldBox.setAlignment(Pos.CENTER);
		
		return new Scene(pane, 800, 800);		
	}
	
	// This method will create the game scene, where user will choose who to bet on and  the amount, then it will display
	// the cards drawn by the server. 
	
	public Scene createGameScene() {
		BorderPane pane = new BorderPane();
		
		// Create Button to allow the user to either place a bet on the result either Player wins , Tie, or Banker wins
		
		Button playerButton = new Button("Player");
		Button tieButton = new Button("Draw");
		Button bankerButton = new Button("Banker");
		playerButton.setStyle("-fx-font-family : Helvetica Neue;");
		tieButton.setStyle("-fx-font-family : Helvetica Neue;");
		bankerButton.setStyle("-fx-font-family : Helvetica Neue;");
		playerButton.setPrefSize(80, 35);
		tieButton.setPrefSize(80, 35);
		bankerButton.setPrefSize(80, 35);
		
	
		playerButton.setOnAction(new EventHandler<ActionEvent> (){ // When bet is placed on the player

			@Override
			public void handle(ActionEvent event) {   // set betChoice to player and change GUI
				betChoice = "Player";
				playerButton.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-text-fill: #FFFFFF;" + "-fx-background-color: #008B8B;");
				tieButton.setStyle("-fx-font-family : Helvetica Neue;");
				bankerButton.setStyle("-fx-font-family : Helvetica Neue;");
				
				if(betAmount != -1 && nextButton.isDisabled() && !animationMode ) {
					playButton.setDisable(false);
				}
				
			}
			
		});
		
		tieButton.setOnAction(new EventHandler<ActionEvent> (){  // When bet is placed on the tie

			@Override
			public void handle(ActionEvent event) {  // set betChoice to draw and change GUI
				betChoice = "Draw";
				tieButton.setStyle("-fx-font-family : Helvetica Neue;"+ "-fx-text-fill: #FFFFFF;"  + "-fx-background-color: #008B8B;");
				playerButton.setStyle("-fx-font-family : Helvetica Neue;");
				bankerButton.setStyle("-fx-font-family : Helvetica Neue;");
				
				if(betAmount != -1 && nextButton.isDisabled() && !animationMode) {
					playButton.setDisable(false);
				}
			}
			
		});
		
		bankerButton.setOnAction(new EventHandler<ActionEvent> (){  // When bet is placed on the banker
			
			@Override
			public void handle(ActionEvent event) { // set betChoice to banker and change GUI
				betChoice = "Banker";
				bankerButton.setStyle("-fx-font-family : Helvetica Neue;"+ "-fx-text-fill: #FFFFFF;" + "-fx-background-color: #008B8B;");
				playerButton.setStyle("-fx-font-family : Helvetica Neue;");
				tieButton.setStyle("-fx-font-family : Helvetica Neue;");
				
				if(betAmount != -1 && nextButton.isDisabled() && !animationMode) {
					playButton.setDisable(false);
				}
				
				
			}
		});
		
		HBox betSpots = new HBox(playerButton,tieButton,bankerButton);	// store button in a HBox since these will be the three options
		
		VBox mainBox = new VBox(betSpots);  // this main box will contain all the important GUI elements
		mainBox.setAlignment(Pos.CENTER);
		betSpots.setAlignment(Pos.CENTER);
		
		playerCards = new ArrayList<ImageView>();   // this arraylist will be used to display player cards 
		bankerCards = new ArrayList<ImageView> (); // this arraylist will be used to display banker cards
		
		Rectangle rect = new Rectangle(100.0,120.0);
		rect.setArcHeight(60.0);
		rect.setArcWidth(60.0);
		
		for(int i = 0; i < 3; i++) { // add just size of image view, set the image to back of the card since cards are not drawn yet
			
			playerCards.add(new ImageView(new Image("Red_back.jpg")));
			playerCards.get(i).setFitWidth(100);
			playerCards.get(i).setFitHeight(120);
			playerCards.get(i).setEffect(new DropShadow(20, Color.BLACK));
			playerCards.get(i).setStyle("-fx-background-radius: 30px;" + "-fx-background-color: transparent;");
				
			bankerCards.add(new ImageView(new Image("Red_back.jpg")));
			bankerCards.get(i).setFitWidth(100);
			bankerCards.get(i).setFitHeight(120);
			bankerCards.get(i).setEffect(new DropShadow(20, Color.BLACK));
			bankerCards.get(i).setStyle( "-fx-border-radius: 30px;" + "-fx-background-color: transparent;");
		}
		
		// Create two HBox's to store player cards and banker cards images
		
		HBox playerCardsBox = new HBox(playerCards.get(0), playerCards.get(1), playerCards.get(2));  
		HBox bankerCardsBox = new HBox(bankerCards.get(0), bankerCards.get(1), bankerCards.get(2));	
		playerCardsBox.setSpacing(20);
		bankerCardsBox.setSpacing(20);
		
		// Create two labels player and banker, so the user will know which side of the screen has player cards and which has banker cards
		
		Label playerLabel = new Label("Player");
		playerLabel.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-text-fill: #C0C0C0;");
		playerLabel.setFont(new Font(35));
		playerLabel.setAlignment(Pos.CENTER);
		
		Label bankerLabel = new Label("Banker");
		bankerLabel.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-text-fill: #C0C0C0;");
		bankerLabel.setFont(new Font(35));
		bankerLabel.setAlignment(Pos.CENTER);
		
		// Create two labels to display the points of the player and banker
		
		playerPointsLabel = new Label("Player Points : ");
		playerPointsLabel.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-text-fill: #C0C0C0;");
		bankerPointsLabel = new Label("Banker Points : ");
		bankerPointsLabel.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-text-fill: #C0C0C0;");
		playerTotal = 0;
		bankerTotal = 0;
		
		VBox playerBox = new VBox(playerLabel, playerPointsLabel,playerCardsBox);
		VBox bankerBox = new VBox(bankerLabel, bankerPointsLabel, bankerCardsBox);		
		playerBox.setSpacing(20);
		bankerBox.setSpacing(20);
		playerBox.setAlignment(Pos.CENTER);
		bankerBox.setAlignment(Pos.CENTER);		
		
		HBox cardBox = new HBox(playerBox, bankerBox);
		cardBox.setAlignment(Pos.CENTER);
		cardBox.setSpacing(60);
		
		// Create Button to control the flow of the game, play sends the bet amount and bet choice to the server and then results are shown,
		// quit button quits the game.
		
		Button quitButton = new Button("Quit");
		quitButton.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-background-color : #808080;");
		quitButton.setOnAction(e -> {
			Platform.exit();
		});
		
		playButton = new Button("Play Round");
		playButton.setDisable(true);
		playButton.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;");
		playButton.setOnAction(e->{
			playButton.setDisable(true);
			connection.setInfo(betAmount, betChoice);
			connection.send();		
		});
		
		nextButton = new Button("Next Round"); // user will click this button if it wants to play next round
		nextButton.setDisable(true);
		nextButton.setStyle("-fx-font-family : Helvetica Neue;" +  "-fx-background-color : #808080;");
		nextButton.setOnAction(e->{
			nextButton.setDisable(true);
			reset();
					
		});
		
		// betField is a text-field to  input the bet amount, and the clicking bet button will set the bet amount 
	
		TextField betField = new TextField();
		betField.setStyle("-fx-font-family : Helvetica Neue;" );
		betField.setPromptText("Bet Amount");
		betField.setMaxSize(100, 40);
		betField.setPrefSize(100, 40);
		
		Button betButton = new Button("Bet");
		betButton.setStyle("-fx-font-family : Helvetica Neue; -fx-background-color : #808080;");
		betButton.setOnAction(new EventHandler<ActionEvent> () {   // set the betAmount to the amount entered in betField 
	
			@Override
			public void handle(ActionEvent event) {
				String regex = "\\d+";
				boolean a = betField.getText().matches(regex);  // perform a regex check 
				
				if(a) {  // set betAmount and enable playButton
					betAmount = Integer.parseInt(betField.getText());
					
					if(nextButton.isDisabled() && betChoice.length() > 0 && !animationMode) {
						playButton.setDisable(false);
					}	
					
				}else { // disable playButton since input is not valid
					playButton.setDisable(true);

				}		
			}		
		});
		
		HBox betBox = new HBox(betField, betButton);
		betButton.setPrefSize(60,30);
		betBox.setAlignment(Pos.CENTER);
		betBox.setPadding(new Insets(0,-50,0,30));
		betBox.setSpacing(30);
		betButton.setPadding(new Insets(4,0,0,0));
		mainBox.getChildren().add(betBox);
		mainBox.setSpacing(40);
		
		HBox controlBox2 = new HBox(playButton, nextButton);
		controlBox2.setSpacing(30);
		HBox controlBox = new HBox(controlBox2, quitButton);
		controlBox2.setAlignment(Pos.CENTER);
		
		controlBox.setSpacing(60);
		controlBox.setAlignment(Pos.CENTER);
		controlBox.setPadding(new Insets(0, -70, 0, 60) );
		playButton.setPrefSize(120, 50);
		quitButton.setPrefSize(80, 50);
		nextButton.setPrefSize(120, 50);
		
		mainBox.getChildren().add(controlBox);
		mainBox.getChildren().add(0, cardBox);
		
		pane.setCenter(mainBox);
		
		// Create Labels to display the result and totalWinnings
		
		resultLabel = new Label();
		resultDescription = new Label();
		resultLabel.setStyle("-fx-font-family : Helvetica Neue;"  + "-fx-text-fill: #FFFFFF;");
		resultDescription.setStyle("-fx-font-family : Helvetica Neue;" + "-fx-text-fill: #FFFFFF;" );
		resultLabel.setFont(new Font(20));
		resultDescription.setFont(new Font(20));
		
		totalWinnings = 0;
		totalWinningsLabel = new Label("Current Winnings : " + Integer.toString(totalWinnings));
		totalWinningsLabel.setStyle("-fx-font-family : Helvetica Neue;"  + "-fx-text-fill: #FFFFFF;");
		totalWinningsLabel.setFont(new Font(25));
		
		VBox resultArea = new VBox(totalWinningsLabel, resultLabel, resultDescription);
		resultArea.setAlignment(Pos.TOP_CENTER);
		resultArea.setSpacing(30);
		
		pane.setTop(resultArea);
		
		pane.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

		return new Scene(pane, 800, 800);
	}
	
	// This method is used to get the image of a given card
	
	public Image cardToImage(int val, String suite) {
		String s = suite.substring(0, 1);
		String v;
		
		// create the correct file name of the card given the card
		
		if(val == 1) {
			v = "A";
		}else if (val == 11){
			v = "J";
		}else if (val == 12) {
			v = "Q";
		}else if(val == 13) {
			v = "K";
		}else {
			v = Integer.toString(val);
		}
		
		String name = v + s + ".jpg";
		
		return new Image(name);
	}
	
	// This method sets the card images and uses pause transition to animate the process of drawing cards.
	
	public void setImages(BaccaratInfo info) {
		i = 0;
		PauseTransition p1 = new PauseTransition(Duration.seconds(0.0));	
		PauseTransition p2 = new PauseTransition(Duration.seconds(1.0));
		PauseTransition p3 = new PauseTransition(Duration.seconds(1.0));
		

		p1.setOnFinished(e -> {  // This will animate the drawn cards of the player
			p1.setDuration(Duration.seconds(1));
			int val = info.getPlayerCardValue(i);
			String suite = info.getPlayerCardSuite(i);
			playerCards.get(i).setImage(cardToImage(val, suite));  // set the playerCard image view to the drawn card image 
			
			if(i < 1) {
				p1.play();
				i += 1;
			}else {  // animated through both the player cards now animate banker cards
				i = 0;
				p2.play();
			}
					
		});
		
		
		p2.setOnFinished(e -> { // This will animate the drawn cards of the banker
			int val2 = info.getBankerCardValue(i);
			String suite2 = info.getBankerCardSuite(i);
			bankerCards.get(i).setImage(cardToImage(val2,suite2));  // set the bankerCard image view to the drawn card image
			
			if(i < 1) { 
				p2.play();
				i += 1;
			}else {  // animated through both the banker cards now  display result or animate more cards and then display result
				i = 0;
				p3.play();
			}
			
		});
		

		
		 // this will display result if natural win or  display more cards if they were drawn and then display the result

		p3.setOnFinished(e -> {	
			
			if(  (info.getNumOfPlayerCards() == 3) || (info.getNumOfBankerCards() == 3) ) {  // third card was drawn for banker or player
				
				if(info.getNumOfPlayerCards() == 3) { // third card was drawn for the player
					
					int val = info.getPlayerCardValue(2);
					String suite = info.getPlayerCardSuite(2);
					playerCards.get(2).setImage(cardToImage(val, suite));	 // set the third imageview of player to the drawn card
					
					if(info.getNumOfBankerCards() == 2) {  // if the banker only drew 2 cards, display result
						pauseDisplayResults(info);
					}
					
				}
				
				 if(info.getNumOfBankerCards() == 3) { // third card was drawn for the banker
					 	PauseTransition p = new PauseTransition(Duration.seconds(1));	 
					 
					 	p.setOnFinished(e2 -> {
					 		int val2 = info.getBankerCardValue(2);
							String suite2 = info.getBankerCardSuite(2);
							bankerCards.get(2).setImage(cardToImage(val2,suite2)); // set the third imageview of banker to the drawn card
						 	pauseDisplayResults(info);  // display result
					 	});
					 	
					 	p.play();		
				 }			
				
			}else { // natural win so display results			
			 	pauseDisplayResults(info);
			}		
		});
				
		p1.play(); // start the animation of drawing starts and displaying cards

	}
	
	// This method will pause for one second and then display the result of a round of the game.
	
	public void pauseDisplayResults(BaccaratInfo info) {
		PauseTransition p = new PauseTransition(Duration.seconds(1));	 
		
	 	p.setOnFinished(e3 -> {
			setScore((BaccaratInfo) info);			
			setResult(((BaccaratInfo) info).getBetPlace(), ((BaccaratInfo) info).getResult());
			setWinnings((BaccaratInfo) info);
			nextButton.setDisable(false);  // disable so user can move on to play the next round
			animationMode = false;
	 	});
	 	
	 	p.play();
	}
	
	// This method will set the player and banker score of the BaccaratInfo game and change the label to display the score for player
	// and banker
	
	public void setScore(BaccaratInfo info) {
		playerTotal = info.getPlayerScore();
		bankerTotal = info.getBankerScore();
		
		playerPointsLabel.setText("Player Points : " + Integer.toString(playerTotal)); // display the player points
		bankerPointsLabel.setText("Banker Points : " + Integer.toString(bankerTotal)); // display banker points
		
	}
	
	// This method will display the result of the game
	
	public void setResult(String betPlace, String result) {
		
		String r1 = (result.equals("Draw")) ? "Draw" : result + " wins";
		resultLabel.setText(r1);
		
		String d1;
		
		if(betPlace.equals(result)){
			d1 = "Congrats, you bet " + betPlace + "! You win!" ;
		}else {
			d1 = "Sorry, you bet " + betPlace + "! You lost your bet!";
		}
		
		resultDescription.setText(d1);
	}
	
	// This method will display the winnings of the game
	
	public void setWinnings(BaccaratInfo info) {
		totalWinnings += info.getWon();
		totalWinningsLabel.setText("Current Winnings : " + Integer.toString(totalWinnings));
	}
	
	
	public void reset() {
		playerTotal = 0;
		bankerTotal = 0;
		
		playerPointsLabel.setText("Player Points : ");
		bankerPointsLabel.setText("Banker Points : ");
		
		resultLabel.setText("");
		resultDescription.setText("");
		
		for(int i = 0; i < 3; i++) {
			playerCards.get(i).setImage(new Image("Red_back.jpg"));
			bankerCards.get(i).setImage(new Image("Red_back.jpg"));
		}
		
		
		playButton.setDisable(false);
		
	}
	
}


