package nl.thewgbbroz.code66.mainprogram.cheatdetector;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.thewgbbroz.code66.CheatString;
import nl.thewgbbroz.code66.CheatStrings;
import nl.thewgbbroz.code66.StringsCollection;
import nl.thewgbbroz.code66.mainprogram.MainProgram;

public class CheatDetector {
	private static final int READ_BUFFER = 1024 * 1000 * 10; // 10mb

	private StringsCollection stringsCollection;

	public CheatDetector(StringsCollection stringsCollection) {
		this.stringsCollection = stringsCollection;
	}

	public CheatDetectionResult detectClients(InputStream in) throws IOException {
		List<CheatDetectionResult> results = detectMultiple(stringsCollection.clientStrings, in);

		CheatDetectionResult max = null;
		int maxDetections = -1;

		for (CheatDetectionResult dr : results) {
			if (dr.matchStrings > maxDetections) {
				max = dr;
				maxDetections = dr.matchStrings;
			}
		}

		if (max == null) {
			System.out.println("No best matched cheat client! Did we get all strings?");
		}

		return max;
	}

	public CheatDetectionResult detectCsrss(InputStream in) throws IOException {
		return detectMultiple(Arrays.asList(stringsCollection.csrssStrings), in).get(0);
	}

	public CheatDetectionResult detectExplorer(InputStream in) throws IOException {
		return detectMultiple(Arrays.asList(stringsCollection.explorerStrings), in).get(0);
	}

	public CheatDetectionResult detectDwm(InputStream in) throws IOException {
		return detectMultiple(Arrays.asList(stringsCollection.dwmStrings), in).get(0);
	}

	private List<CheatDetectionResult> detectMultiple(List<CheatStrings> cheatStrings, InputStream in) throws IOException {
		Map<CheatStrings, Map<CheatString, Integer>> previousMatches = new HashMap<>();
		for (CheatStrings cs : cheatStrings) {
			previousMatches.put(cs, new HashMap<>());
		}

		int totalRead = 0;
		int totalSize = in.available();

		byte[] buf = new byte[READ_BUFFER];
		int read;
		while ((read = in.read(buf)) > 0) {
			String str = new String(buf, 0, read);

			for (CheatStrings cs : cheatStrings) {
				Map<CheatString, Integer> stringMatches = previousMatches.get(cs);

				cs.update(str, stringMatches);
			}

			totalRead += read;
			int percent = (int) ((double) totalRead / (double) totalSize * 100d);
			// System.out.println("Read " + totalRead + "/" + totalSize + " (" + percent +
			// "%)");
			MainProgram.setStatus("Reading file " + percent + "%");
		}

		MainProgram.setStatus(MainProgram.PROCESSING);

		List<CheatDetectionResult> result = new ArrayList<>();

		for (CheatStrings cs : previousMatches.keySet()) {
			int matches = 0;

			CheatString highestString = null;
			int highestStringFreq = -1;

			Map<CheatString, Integer> stringFrequencies = previousMatches.get(cs);
			for (CheatString string : stringFrequencies.keySet()) {
				int freq = stringFrequencies.get(string);
				if (freq >= string.minFrequency) {
					// This string got matched
					matches++;

					if (freq > highestStringFreq) {
						highestString = string;
						highestStringFreq = freq;
					}
				}
			}

			result.add(new CheatDetectionResult(cs, highestString, matches));
		}

		return result;
	}
}
