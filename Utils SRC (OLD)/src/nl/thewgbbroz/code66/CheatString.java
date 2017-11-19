package nl.thewgbbroz.code66;

public class CheatString {
	public final String string;
	public final int minFrequency;
	
	public CheatString(String string, int minFrequency) {
		this.string = string;
		this.minFrequency = minFrequency;
	}
	
	public CheatString(String str) {
		this(str, 1);
	}
}
