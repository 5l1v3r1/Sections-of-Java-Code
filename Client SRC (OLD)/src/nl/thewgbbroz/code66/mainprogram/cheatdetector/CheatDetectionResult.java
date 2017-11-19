package nl.thewgbbroz.code66.mainprogram.cheatdetector;

import nl.thewgbbroz.code66.CheatString;
import nl.thewgbbroz.code66.CheatStrings;

public class CheatDetectionResult {
	public final CheatStrings match;
	public final CheatString exactMatch;
	public final int matchStrings;
	public final int matchMaxStrings;

	public CheatDetectionResult(CheatStrings match, CheatString exactMatch, int matchStrings) {
		this.match = match;
		this.exactMatch = exactMatch;
		this.matchStrings = matchStrings;
		this.matchMaxStrings = match == null ? 0 : match.getCheatStrings().size();
	}

	@Override
	public String toString() {
		return "[" + (match == null ? "null" : match.getClientName()) + ", " + matchStrings + "/" + matchMaxStrings + "]";
	}
}
