import java.util.ArrayList;

public class BaccaratGame {
	
	public ArrayList<Card> playerHand; // store player hand
	public ArrayList<Card> bankerHand; // store banker hand
	public BaccaratDealer theDealer; 
	public double currentBet;       
	public double totalWinnings;
	public String result;         // store the result of the game
	
	public BaccaratGame() {
		this.theDealer = new BaccaratDealer();
		this.totalWinnings = 0.0;
		playerHand = new ArrayList<Card>();
		bankerHand = new ArrayList<Card>();
	}

	// Plays a round of Baccarat Game and returns the amount won based on the bet and the result
	
	public double evaluateWinnings() {
		theDealer.shuffleDeck();  // generate deck and shuffle
		playerHand = theDealer.dealHand();  // draw two cards for player
		bankerHand = theDealer.dealHand();  // draw two cards for banker
		Card playerCard = null;
		
		// If game is not a natural win
		
		if(BaccaratGameLogic.handTotal(playerHand) < 8 && BaccaratGameLogic.handTotal(bankerHand) < 8 ) {
			
			if(BaccaratGameLogic.evaluatePlayerDraw(playerHand)) { // player needs to draw another card
				playerCard = theDealer.drawOne(); // draw a card 
				playerHand.add(playerCard);   // add the card
			}
			
			if(BaccaratGameLogic.evaluateBankerDraw(bankerHand, playerCard)) {  // banker needs to draw another card
				bankerHand.add(theDealer.drawOne());  // draw a card
			}		
			
		}
		
		// find out who won the game
		
		result = BaccaratGameLogic.whoWon(playerHand, bankerHand); 
			
		
			
		if(result.equals("Banker")) {  
			totalWinnings = (currentBet * 1.95);
		}else if(result.equals("Draw")){
			totalWinnings =  (currentBet * 8);
		}else {
			totalWinnings =  (currentBet * 2);
		}
			
		
		
		return totalWinnings;
	}

	
}
