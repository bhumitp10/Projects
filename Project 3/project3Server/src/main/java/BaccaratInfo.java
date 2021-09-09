import java.io.Serializable;
import java.util.ArrayList;

public class BaccaratInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int NumOfPlayerCards; 
	private int NumOfBankerCards;
	
	private ArrayList<Integer> playerCardsValues;  // store values of the player cards
	private ArrayList<String> playerCardsSuites;   // store suites of the player cards
	private ArrayList<Integer> bankerCardsValues;  // store values of the banker cards
	private ArrayList<String> bankerCardsSuites;   // store suites of the banker cards

	private String result;       // store result of the game 
	private String betPlace;     // store the result player bet on
	private int betAmount;       //  store bet amount
	private int won;             // store amount won
	private int playerScore;    
	private int bankerScore;
	
	public int client;

	// getters and setters of data members

	public int getPlayerScore() {
		return playerScore;
	}

	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}

	public int getBankerScore() {
		return bankerScore;
	}
	
	public void setBankerScore(int bankerScore) {
		this.bankerScore = bankerScore;
	}

	BaccaratInfo(){
		playerCardsValues = new ArrayList<Integer>();
		playerCardsSuites = new ArrayList<String>();
		bankerCardsValues = new ArrayList<Integer>();
		bankerCardsSuites = new ArrayList<String>();

	}
	
	
	public void addBankerCard(int value, String suite) {
		bankerCardsValues.add(value);
		bankerCardsSuites.add(suite);
	}
	
	public void addPlayerCard(int value, String suite) {
		playerCardsValues.add(value);
		playerCardsSuites.add(suite);
	}
	
	public int getBankerCardValue(int i) {
		return bankerCardsValues.get(i);
	}
	
	public int getPlayerCardValue(int i) {
		return playerCardsValues.get(i);
	}
	
	public String getPlayerCardSuite(int i) {
		return playerCardsSuites.get(i);
	}
	
	public String getBankerCardSuite(int i) {
		return bankerCardsSuites.get(i);
	}
	
	public int getNumOfPlayerCards() {
		return NumOfPlayerCards;
	}
	
	public int getNumOfBankerCards() {
		return NumOfBankerCards;
	}
	
	public void setNumOfPlayerCards(int numOfPlayerCards) {
		NumOfPlayerCards = numOfPlayerCards;
	}
	
	public void setNumOfBankerCards(int numOfBankerCards) {
		NumOfBankerCards = numOfBankerCards;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getBetPlace() {
		return betPlace;
	}
	
	public void setBetPlace(String betPlace) {
		this.betPlace = betPlace;
	}
	
	public int getBetAmount() {
		return betAmount;
	}
	
	public void setBetAmount(int betAmount) {
		this.betAmount = betAmount;
	}
	
	public int getWon() {
		return won;
	}

	public void setWon(int won) {
		this.won = won;
	}

}
