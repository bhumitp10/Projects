import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;




class MyTest {
	BaccaratDealer theDealer;
	ArrayList<Card> deck;
	
	@BeforeEach
	void init(){
		 theDealer = new BaccaratDealer();
		 deck = getDeck();

	}
	
	 ArrayList<Card> getDeck() {
		ArrayList<Card> deck = new ArrayList<Card> ();
		String suite = "Clubs";
		
		for(int j = 0; j < 4; j++) {
		
			for(int i = 1; i <= 10; i++) {
				deck.add(new Card(suite,i));
			
				if(i == 10) {
					deck.add(new Card (suite, 11)); // jack
					deck.add(new Card (suite, 12)); // queen
					deck.add(new Card (suite, 13)); // king
				}	
			}
			
			if(j == 0) {  
				suite = "Diamonds";
			}else if(j == 1) {
				suite = "Hearts";
			}else if(j == 2) {
				suite = "Spades";
			}	
			
		}
		
		return deck;
	}
	
	 // Tests for Card Class
	 
	@Test
	void card_test1() {
		Card card = new Card("Spades", 8);
		assertEquals(card.suite, "Spades");
		assertEquals(card.value, 8);
	}
	
	@Test
	void card_test2() {
		Card card = new Card("Diamonds", 3);
		assertEquals(card.suite, "Diamonds");
		assertEquals(card.value, 3);
	}
	
	// Test for BaccaratDealer Class
	
	void BaccaratDealerConstructorTest() {
		BaccaratDealer dealer = new BaccaratDealer();
		assertEquals(dealer.deck.size(), 0 );
	}
	
	
	// method to see if a card exists in a given array list of cards
	
	Boolean cardExists(Card c, ArrayList<Card> cards) {
		for(int i = 0; i < cards.size(); i++) {
			if(cards.get(i).suite.equals(c.suite) && ( cards.get(i).value == c.value   ) ) {
				return true;
			}
		}
		
		return false;
	}
	
	// Testing generateDeck() method
	
	@Test
	void generateDeckTest1(){
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.generateDeck();
		ArrayList<Card> deck1 = dealer.deck;
		
		for(int i = 0; i < deck1.size(); i++) {
			assertEquals(cardExists(deck1.get(i), deck), true);
		}
		
	}
	
	@Test
	void generateDeckTest2(){
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.generateDeck();
		ArrayList<Card> deck1 = dealer.deck;
		
		for(int i = 0; i < deck1.size(); i++) {
			assertEquals(cardExists(deck1.get(i), deck), true);
		}	
	}
	
	boolean isCard(String suite, int val) {
		
		if(suite.equals("Clubs") || suite.equals("Spades") || suite.equals("Diamonds") || suite.equals("Hearts")) {
			return (val >= 1 && val <= 13);
		}

		return false;
	}
	
	boolean cardCompare(Card c1, Card c2) {
		return c1.suite.equals(c2.suite) && (c1.value == c2.value)  ;
	}
	
	// Testing dealHand() method
	
	@Test
	void dealHandtest1() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		ArrayList<Card> hand = dealer.dealHand();
		Card c1 = hand.get(0);
		Card c2 = hand.get(1);
		
		assertEquals(true, isCard(c1.suite, c1.value));
		assertEquals(true, isCard(c2.suite, c2.value));
		assertEquals(hand.size(),2);
		assertNotEquals(true, cardCompare(c1,c2));
	}
	
	@Test
	void dealHandtest2() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		ArrayList<Card> hand = dealer.dealHand();
		Card c1 = hand.get(0);
		Card c2 = hand.get(1);
		
		assertEquals(true, isCard(c1.suite, c1.value));
		assertEquals(true, isCard(c2.suite, c2.value));
		assertEquals(hand.size(),2);
		assertEquals(50, dealer.deck.size());
	}
	
	// Testing drawOne() method
	
	@Test
	void drawOneTest1() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		Card c1 = dealer.drawOne();
		assertEquals(isCard(c1.suite,c1.value), true);
		assertEquals(dealer.deck.size(), 51);
		assertEquals(cardExists(c1, dealer.deck), false);
	}
	
	@Test
	void drawOneTest2() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		Card c1 = dealer.drawOne();
		assertEquals(isCard(c1.suite,c1.value), true);
		assertEquals(dealer.deck.size(), 51);
		assertEquals(cardExists(c1, dealer.deck), false);
	}
	
	// Testing shuffleDeck() method
	
	@Test 
	void shuffleDeck1() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		ArrayList<Card> d1 = dealer.deck;
		
		for(int i = 0; i < d1.size(); i++) {
			assertEquals(cardExists(d1.get(i), deck), true);
		}
		assertEquals(d1.size(), 52);
	}
	
	@Test 
	void shuffleDeck2() {
		BaccaratDealer dealer = new BaccaratDealer();
		dealer.shuffleDeck();
		ArrayList<Card> d1 = dealer.deck;
		
		for(int i = 0; i < d1.size(); i++) {
			assertEquals(cardExists(d1.get(i), deck), true);
		}
		assertEquals(d1.size(), 52);
	}
	
	// Testing deckSize() method
	
	
	
	@Test
	void deckSizeTest1() {
		BaccaratDealer dealer = new BaccaratDealer();
		assertEquals(dealer.deckSize(), 0);
		dealer.shuffleDeck();
		assertEquals(dealer.deckSize(), 52);
		dealer.drawOne();
		assertEquals(dealer.deckSize(), 51);
		dealer.dealHand();
		assertEquals(dealer.deckSize(), 49);
		
	}
	
	@Test
	void deckSizeTest2() {
		BaccaratDealer dealer = new BaccaratDealer();
		assertEquals(dealer.deckSize(), 0);
		dealer.shuffleDeck();
		dealer.dealHand();
		dealer.dealHand();
		dealer.dealHand();
		assertEquals(dealer.deckSize(), 46);
		dealer.drawOne();
		assertEquals(dealer.deckSize(), 45);
		dealer.dealHand();
		dealer.dealHand();
		assertEquals(dealer.deckSize(), 41);

	}
	
	// Testing BaccaratGameLogic
	
	// Testing whoWon() method, a natural win for the player
	
	@Test
	void whoWonTest1() { 
		ArrayList<Card> hand1 = new ArrayList<Card>(); // player hand
		ArrayList<Card> hand2 = new ArrayList<Card>(); // banker hand

		hand1.add(new Card("Spades", 4));
		hand1.add(new Card("Hearts", 5));
		hand2.add(new Card("Diamonds", 2));
		hand2.add(new Card("Diamonds", 3));
		
		assertEquals(BaccaratGameLogic.whoWon(hand1, hand2), "Player");
	}
	
	// Testing whoWon() method, a unnatural win for the banker
	
	@Test
	void whoWonTest2() { 
		ArrayList<Card> hand1 = new ArrayList<Card>(); // player hand
		ArrayList<Card> hand2 = new ArrayList<Card>(); // banker hand

		hand1.add(new Card("Clubs", 3));
		hand1.add(new Card("Hearts", 3));
		hand2.add(new Card("Spades", 4));
		hand2.add(new Card("Diamonds", 2));
		
		assertEquals(BaccaratGameLogic.whoWon(hand1, hand2), "Draw");
	}
	
	// Testing handTotal() method
	
	@Test
	void handTotalTest1() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card("Diamonds", 10));
		hand1.add(new Card("Clubs", 4));
		hand1.add(new Card("Spades", 3));
		
		assertEquals(BaccaratGameLogic.handTotal(hand1), 7);
	}
	
	@Test
	void handTotalTest2() {
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card("Diamonds", 7));
		hand1.add(new Card("Clubs", 4));
		hand1.add(new Card("Hearts", 7));
		
		assertEquals(BaccaratGameLogic.handTotal(hand1), 8);
	}
	
	// Testing evaluateBankerDraw() method 
	
	@Test
	void evaluateBankerDrawTest1() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("Spades", 10));
		hand.add(new Card("Clubs", 3));
		
		assertEquals(BaccaratGameLogic.evaluateBankerDraw(hand, new Card("Diamonds",5)), true  );
	}
	

	@Test
	void evaluateBankerDrawTest2() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("Hearts", 4));
		hand.add(new Card("Diamonds", 2));
		
		assertEquals(BaccaratGameLogic.evaluateBankerDraw(hand, new Card("Spades",2)), false  );
	}
	
	// Tests for when player does not need to draw a card
	
	@Test
	void evaluatePlayerDrawTest1() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("Spades", 4));
		hand.add(new Card("Hearts", 3));
		
		assertEquals(BaccaratGameLogic.evaluatePlayerDraw(hand), false  );
	}
	
	// Tests for when player needs to draw a card
	
	@Test
	void evaluatePlayerDrawTest2() {
		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(new Card("Diamonds", 2));
		hand.add(new Card("Spades", 13));
		
		assertEquals(BaccaratGameLogic.evaluatePlayerDraw(hand), true  );
	}
	
	
	// Testing BaccaratGame class
	
	void BaccaratGameConstructorTest() {
		BaccaratGame game = new BaccaratGame();
		assertNotEquals(game.theDealer, null);
		assertEquals(game.totalWinnings, 0.0);
		assertEquals(game.playerHand.size(), 0);
		assertEquals(game.bankerHand.size(), 0);
	}
	
	// Testing different bet amounts for evaluateWinnings depending on the result
	
	void evaluateWinningsTest1() {
		int bet = 250;
		BaccaratGame game = new BaccaratGame();
		game.currentBet = bet;
		
		int amount = (int)game.evaluateWinnings();
		
		if(game.result.equals("Player")) {
			int win = bet * 2;
			assertEquals(win, amount);
		}else if(game.result.equals("Banker")) {
			int win = (int) (bet * 1.95);
			assertEquals(win, amount);
		}else {
			int win = bet * 8;
			assertEquals(win, amount);
		}
		
	}
	
	void evaluateWinningsTest2() {
		int bet = 500;
		BaccaratGame game = new BaccaratGame();
		game.currentBet = bet;
		
		int amount = (int)game.evaluateWinnings();
		
		if(game.result.equals("Player")) {
			int win = bet * 2;
			assertEquals(win, amount);
		}else if(game.result.equals("Banker")) {
			int win = (int) (bet * 1.95);
			assertEquals(win, amount);
		}else {
			int win = bet * 8;
			assertEquals(win, amount);
		}
		
	}
	
	void evaluateWinningsTest3() {
		int bet = 300;
		BaccaratGame game = new BaccaratGame();
		game.currentBet = bet;
		
		int amount = (int)(game.evaluateWinnings());
		
		if(game.result.equals("Player")) {
			int win = bet * 2;
			assertEquals(win, amount);
		}else if(game.result.equals("Banker")) {
			int win = (int) (bet * 1.95);
			assertEquals(win, amount);
		}else {
			int win = bet * 8;
			assertEquals(win, amount);
		}
		
	}

}
