
public class Card {
	 public String suite;  // suite of card
	 public int value;   // value of card
	
	public Card(String suite, int value) {
		this.suite = suite;  // set suite
		this.value = value;  // set value
	}
	
	// getters and setters

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}

