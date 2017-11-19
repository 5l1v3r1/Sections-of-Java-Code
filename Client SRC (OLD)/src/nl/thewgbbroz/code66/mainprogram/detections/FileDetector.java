package nl.thewgbbroz.code66.mainprogram.detections;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

import nl.thewgbbroz.code66.mainprogram.cheatdetector.CheatDetectionResult;
import nl.thewgbbroz.code66.mainprogram.cheatdetector.CheatDetector;

public class FileDetector extends Detector {
	public static final int CLIENTS = 0;
	public static final int CSRSS = 1;
	public static final int EXPLORER = 2;
	public static final int DWM = 3;

	private CheatDetector cheatDetector;
	private File file;
	private int type;

	public FileDetector(Shell shell, int x, int y, String labelText, Color forgroundColor, CheatDetector cheatDetector, int type) {
		super(shell, x, y, labelText, forgroundColor);

		this.cheatDetector = cheatDetector;
		this.type = type;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public DetectionResult getDetectionResult() {
		if (file == null || !file.exists()) {
			System.out.println("No or invalid file selected. Skipping detection.");
			return new DetectionResult("Skipped.");
		}

		CheatDetectionResult result;

		try {
			FileInputStream fis = new FileInputStream(file);

			if (type == CLIENTS)
				result = cheatDetector.detectClients(fis);
			else if (type == CSRSS)
				result = cheatDetector.detectCsrss(fis);
			else if (type == EXPLORER)
				result = cheatDetector.detectExplorer(fis);
			else if (type == DWM)
				result = cheatDetector.detectDwm(fis);
			else {
				fis.close();
				throw new IllegalStateException("Invalid type.");
			}
		} catch (IOException e) {
			return new DetectionResult(e.toString());
		}

		if (result != null && result.matchStrings > 0) {
			setCheckmark(RED);
			System.out.println("File cheats detected in " + file.getName() + "!");

			String str = "Cheats detected";
			if (type == CLIENTS) {
				str += " \"" + result.match.getClientName() + "\"";
			} else if (type == CSRSS) {
				str += " (" + result.exactMatch.string + ")";
			}

			str += ".";

			return new DetectionResult(str);
		} else {
			setCheckmark(GREEN);

			return new DetectionResult("Found nothing.");
		}
	}
}
