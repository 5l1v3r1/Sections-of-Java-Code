package nl.thewgbbroz.code66.mainprogram.detections;

import java.io.File;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

import nl.thewgbbroz.code66.utils.Utils;

public class RecycleBinDetector extends Detector {
	private static final int DETECTION_TIME = 60 * 15; // 15 minutes

	public RecycleBinDetector(Shell shell, int x, int y, String labelText, Color forgroundColor) {
		super(shell, x, y, labelText, forgroundColor);
	}

	@Override
	public DetectionResult getDetectionResult() {
		File path = new File("C:/$Recycle.Bin");

		long lastModified = Utils.getLastModified(path);
		long secs = (System.currentTimeMillis() - lastModified) / 1000;
		System.out.println("The recycle bin was modified " + secs + " seconds ago.");

		if (secs > DETECTION_TIME) {
			setCheckmark(GREEN);
		} else {
			setCheckmark(RED);
		}

		return new DetectionResult("The recycle bin was modified " + Utils.formatTime((int) secs) + " ago.");
	}
}
