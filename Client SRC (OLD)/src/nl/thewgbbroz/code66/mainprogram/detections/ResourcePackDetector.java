package nl.thewgbbroz.code66.mainprogram.detections;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

public class ResourcePackDetector extends Detector {

	public ResourcePackDetector(Shell shell, int x, int y, String labelText, Color forgroundColor) {
		super(shell, x, y, labelText, forgroundColor);
	}

	@Override
	public DetectionResult getDetectionResult() {
		File path = new File(System.getenv("APPDATA"), ".minecraft/resourcepacks/");
		List<String> detectedPacks = new ArrayList<String>();

		for (File file : path.listFiles()) {
			try {
				if ((file.isDirectory() && scanFolder(file)) || file.getName().toLowerCase().endsWith(".zip") && scanZip(file)) {
					detectedPacks.add(file.getPath());
				}
			} catch (Throwable e) {
				e.printStackTrace();
				continue;
			}
		}

		if (detectedPacks.size() > 0) {
			setCheckmark(RED);
			StringBuilder sb = new StringBuilder("X-Ray resource pack(s) detected:");
			String sep = System.lineSeparator();
			detectedPacks.forEach(s -> sb.append(sep).append(s));
			return new DetectionResult(sb.toString());
		} else {
			setCheckmark(GREEN);
		}
		return new DetectionResult("No X-Ray resource packs detected.");
	}

	private boolean scanFolder(File path) throws IOException {
		File file = new File(path, "assets/minecraft/textures/blocks/stone.png");

		if (!file.exists()) {
			return false;
		}

		BufferedImage image = ImageIO.read(file);

		if (hasTransparentPixel(image)) {
			return true;
		}
		return false;
	}

	private boolean scanZip(File path) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(path);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();

			if (name.equals("assets/minecraft/textures/blocks/stone.png")) {
				InputStream is = zipFile.getInputStream(entry);
				BufferedImage image = ImageIO.read(is);
				if (hasTransparentPixel(image)) {
					return true;
				}
				is.close();
			}
		}
		zipFile.close();
		return false;
	}

	private boolean hasTransparentPixel(BufferedImage image) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if ((image.getRGB(x, y) >> 24 & 0xFF) <= 170) {
					return true;
				}
			}
		}
		return false;
	}
}