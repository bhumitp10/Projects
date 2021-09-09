import java.util.ArrayList;

public class BaccaratGameLogic {
	
	// Returns the result of the game given two hands.
	
	public static String whoWon(ArrayList<Card> hand1, ArrayList<Card> hand2) {
		int points1 = BaccaratGameLogic.handTotal(hand1); // get player points
		int points2 = BaccaratGameLogic.handTotal(hand2); // get banker points 
		
		if(points1 > points2) { // player wins
			return "Player";
		}else if(points1 < points2) {  // banker wins
			return "Banker";
		}

		return "Draw";     
	}

	// Count the points of the given hand.
	
	public static int handTotal(ArrayList<Card> hand) {
		int count = 0;
		
		
		for(int i = 0; i < hand.size(); i++) {
			Card c = hand.get(i);
			int val = c.getValue();
			
			if(val >= 1 && val <= 9) {  // only count value if it is between 1(inclusive) and 9 (inslusive)
				count += val;
			}
			
			count = count % 10; // score can only be 9 at most so mod 10
		}	
		
		return count;
	}
	
	// Return true if banker needs to draw another card, else false.
	
	public static boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard) {
		int count = handTotal(hand);  // get total score of hand
		
			
		if(count <= 2) {  // if score is less than or equal to 2
			return true;
		}else if( count >= 3 && count <= 6) {         // between 3(inclusive) and 6(inclusive)
			
			if(playerCard == null && count != 6) {   // player card is null then card should be drawn
				return true;
			}	
				
			if(count == 3 ) {    // score is 3 
						
				return (playerCard.getValue() != 8);  // if not equal to 8 then card should be drawn
				
			}else if(count == 4) {			
				
				int val = playerCard.getValue();    
				
				return val >= 2 && val <= 7;	// value of playaer's card is between 2(inclusive) and 7(inclusive)
				
			}else if(count == 5) {
							
				int val = playerCard.getValue();   
				
				return val >= 4 && val <= 7;   // value of player's card is between between 4(inclusive) and 7(inclusive)
				
			}else if(playerCard != null){
				return (playerCard.getValue() == 6 || playerCard.getValue() == 7) ; // value of player's card is 6 or 7
			}
			
		}
			
		return false;
	}
	
	// Return true if player needs another card false otherwise
	
	public static boolean evaluatePlayerDraw(ArrayList<Card> hand) {
		int count = handTotal(hand);
		
		return (count <= 5);  // player should draw card if it's score is less than or equal to 5
	}

}
