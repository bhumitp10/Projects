import java.util.ArrayList;
import java.util.Random;

public class BaccaratDealer {
	 
	public ArrayList<Card> deck; // store the cards
	 
	 
	public BaccaratDealer() {
		deck = new ArrayList<Card>();
	}
	
	// Generate a 52 card deck;

	public void generateDeck() {
		deck.clear();
		
		String suite = "Clubs";
	
		for(int j = 0;j < 4; j++) {  // loops for each suite
		
			for(int i = 1; i <= 10; i++) { // inner loop
				
				deck.add(new Card(suite, i)); // regular card add
				
				if(i == 10) {  // add the face cards
					
					// we will use unique value of these cards to identify them and later we will give them their actual value
					// even though, ace is worth 1 point, and other face are zero points, we will use their values to identify
					// later on we can still get their values since we know the card type
					
					deck.add(new Card(suite, 11)); // Jack
					deck.add(new Card(suite, 12)); // Queen
					deck.add(new Card(suite, 13)); // King
					
					// change suite
					
					if(j == 0) {  
						suite = "Diamonds";
					}else if(j == 1) {
						suite = "Hearts";
					}else if(j == 2) {
						suite = "Spades";
					}
					
				}
				
			}
		}
		
	}
	
	// This method will deal two random cards from the deck and return an array-list of those cards
	
	public ArrayList<Card> dealHand(){
		
		ArrayList<Card> hand = new ArrayList<Card> ();
		
		Random rand = new Random(); 
		
		int x = rand.nextInt(deck.size());  // get a random number within range of deck's index
		hand.add(deck.get(x));   
		deck.remove(x); // remove card from deck
		
		int y = rand.nextInt(deck.size());	 // get a random number within range of deck's index
		hand.add(deck.get(y));
		deck.remove(y);		 // remove card  from deck	
		
		return hand;  
	}
	
	// This method draws one card from the deck and returns it 
	
	public Card drawOne() {
		
		Random rand = new Random();
		int x = rand.nextInt(deck.size());
		Card card = deck.get(x);
		deck.remove(x); // remove card from deck
		
		return card;
	}
	
	// Generate new deck of 52 cards and shuffle the deck by randomizing the cards in it
	
	public void shuffleDeck() {
		generateDeck();  // generate new deck of 52 cards
		Random rand = new Random();

		
		for(int i = 0; i < 200; i++) {
			
			int x = rand.nextInt(52);
			int y = rand.nextInt(52);			
			
			while( x == y) { // if x equals y then keep getting random int for y until they are not equal
				y = rand.nextInt(52);
			}
			
			// swap the two cards
			
			Card temp = deck.get(x);  
			deck.set(x, deck.get(y));
			deck.set(y, temp);
			
		}	
		
	}
	
	// returns the size of the deck
	
	public int deckSize() {
		return deck.size();
	}
	
	public ArrayList<Card> getList() {
		return deck;
	}
	
	
}
