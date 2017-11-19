package nl.thewgbbroz.code66.mainprogram.detections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public abstract class Detector {
	private static final Image CHECK_GREEN, CHECK_RED, CHECK_GRAY;

	public static final int GREEN = 0;
	public static final int RED = 1;
	public static final int GRAY = 2;

	static {
		CHECK_GREEN = new Image(Display.getDefault(), Detector.class.getResourceAsStream("/checkmark_green.png"));
		CHECK_RED = new Image(Display.getDefault(), Detector.class.getResourceAsStream("/checkmark_red.png"));
		CHECK_GRAY = new Image(Display.getDefault(), Detector.class.getResourceAsStream("/checkmark_gray.png"));
	}

	private Label checkmark;

	public Detector(Shell shell, int x, int y, String labelText, Color forgroundColor) {
		checkmark = new Label(shell, SWT.NONE);
		checkmark.setBounds(x, y, 48, 48);
		checkmark.setBackground(new Color(Display.getDefault(), 0, 0, 0, 0));
		setCheckmark(GRAY);

		Label label = new Label(shell, SWT.NONE);
		label.setText(labelText);
		label.setBounds(x - 48 / 2, y - 25, 100, 20);
		label.setBackground(new Color(Display.getDefault(), 0, 0, 0, 0));
		label.setForeground(forgroundColor);
		label.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		label.setAlignment(SWT.CENTER);
	}

	public abstract DetectionResult getDetectionResult();

	public void setCheckmark(final int clr) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				Image img;
				switch (clr) {
				case GREEN:
					img = CHECK_GREEN;
					break;
				case RED:
					img = CHECK_RED;
					break;
				case GRAY:
					img = CHECK_GRAY;
					break;
				default:
					throw new IllegalArgumentException("Invalid color.");
				}

				checkmark.setImage(img);
			}
		});
	}
}
