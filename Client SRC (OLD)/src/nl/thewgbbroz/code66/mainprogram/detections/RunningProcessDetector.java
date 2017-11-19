package nl.thewgbbroz.code66.mainprogram.detections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

import nl.thewgbbroz.code66.mainprogram.MainProgram;

public class RunningProcessDetector extends Detector {
	private String name;
	private List<String> processNames = new ArrayList<>();

	public RunningProcessDetector(Shell shell, int x, int y, String labelText, Color forgroundColor, String processNamesResourceFile) {
		super(shell, x, y, labelText, forgroundColor);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(processNamesResourceFile)));
			name = reader.readLine(); // First line is the name

			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0)
					processNames.add(line.toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read process names from resource file.");
		}
	}

	@Override
	public DetectionResult getDetectionResult() {
		MainProgram.setStatus("Looking for recording software..");

		try {
			String cmd = System.getenv("windir") + "/system32/tasklist.exe";

			Process proc = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;

			// Output header
			reader.readLine();
			reader.readLine();

			// Read equal sign amount
			line = reader.readLine();
			int maxProcNameLength = line.indexOf(" ");

			int amount = 0;
			while ((line = reader.readLine()) != null) {
				line = line.substring(0, maxProcNameLength).trim();
				if (processNames.contains(line.toLowerCase())) {
					System.out.println("Found a process we ware looking for! " + line);
					amount++;
				}
			}

			reader.close();

			if (amount == 0) {
				setCheckmark(GREEN);
				return new DetectionResult("Found no " + name + ".");
			} else {
				setCheckmark(RED);
				return new DetectionResult("Found " + name + " (" + amount + ").");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not check active processes.");

			return new DetectionResult(e.toString());
		}
	}
}
