package nl.thewgbbroz.code66.mainprogram.detections;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import nl.thewgbbroz.code66.mainprogram.MainProgram;

public class ModDetector extends Detector {
	private List<String> cheatStrings = new ArrayList<>();

	public ModDetector(Shell shell, int x, int y, String labelText, Color forgroundColor, String stringsResourceFile) {
		super(shell, x, y, labelText, forgroundColor);

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(stringsResourceFile)));

			reader.readLine(); // First line is the name, we don't need that.

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0)
					cheatStrings.add(line);
			}
		} catch (IOException e) {
			System.out.println("Could not load mod cheat strings as a resource file!");
			e.printStackTrace();
		}
	}

	@Override
	public DetectionResult getDetectionResult() {
		MainProgram.setStatus("Looking for mods..");

		File dotMinecraft = new File(System.getenv("APPDATA"), ".minecraft/");
		List<File> mods = detectMods(dotMinecraft);

		List<ModSummary> modSummaries = new ArrayList<>();

		if (mods.isEmpty()) {
			setCheckmark(GREEN);

			return new DetectionResult("Found no mods.");
		} else {
			setCheckmark(GRAY);

			int totalMods = 0;
			int possibleCheatMods = 0;

			for (File mod : mods) {
				totalMods++;

				try {
					ModSummary modSummary = scanJar(mod);
					if (modSummary.possibleCheats > 0) {
						possibleCheatMods++;
					}

					modSummaries.add(modSummary);
				} catch (IOException e) {
					System.out.println("Error scanning jar file: " + e.toString());
				}
			}

			if (possibleCheatMods > 0) {
				setCheckmark(RED);

				StringBuilder modsSb = new StringBuilder();

				for (ModSummary ms : modSummaries) {
					if (ms.possibleCheats > 0) {
						String mainPackageName = ms.getMainClassPackage();
						if (mainPackageName != null) {
							modsSb.append("[" + ms.getMainClassPackage() + "]");
							modsSb.append(", ");
						}
					}
				}
				modsSb.delete(modsSb.length() - 2, modsSb.length());

				return new DetectionResult("Found " + totalMods + " mods. " + possibleCheatMods + " mods could possibly be a cheat. " + modsSb.toString() + ".");
			} else {
				setCheckmark(GREEN);

				return new DetectionResult("Found " + totalMods + " mods. None contained any possible cheat.");
			}
		}
	}

	private List<File> detectMods(File dir) {
		List<File> res = new ArrayList<>();

		if (dir.getName().equalsIgnoreCase("mods")) {
			// All jar files are mods!
			for (File f : dir.listFiles()) {
				if (f.isFile() && f.getName().toLowerCase().endsWith(".jar")) {
					res.add(f);
				}
			}
		}

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				res.addAll(detectMods(f));
			}
		}

		return res;
	}

	private ModSummary scanJar(File file) throws IOException {
		JarFile jarFile = new JarFile(file.getPath());
		Enumeration<? extends JarEntry> entries = jarFile.entries();
		InputStream in = null;

		int possibleCheats = 0;
		List<String> classNames = new ArrayList<>();

		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();

			in = jarFile.getInputStream(entry);

			if (entry.getName().endsWith(".class")) {
				ModClassSummary classSummary = scanClass(in, jarFile.getName());
				possibleCheats += classSummary.possibleCheats;
				classNames.add(classSummary.className);
			}
		}
		in.close();
		jarFile.close();

		return new ModSummary(possibleCheats, classNames);
	}

	private ModClassSummary scanClass(InputStream in, String path) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] b = new byte[2048];
		int length;

		while ((length = in.read(b)) > 0) {
			bos.write(b, 0, length);
		}

		in.close();

		byte[] classBytes = bos.toByteArray();

		bos.close();

		ClassNode classNode = new ClassNode();

		try {
			new ClassReader(classBytes).accept(classNode, ClassReader.SKIP_DEBUG);
		} catch (Exception e) {
			System.out.println("Error while initializing ClassReader object. " + e.toString());
		}

		if (classNode.name == null) {
			System.out.println("ClassNode's name is null.");
			return new ModClassSummary(0, "");
		}

		String className = classNode.name.replaceAll("/", ".");
		int possibleCheats = 0;

		for (Object o : classNode.methods) {
			if (o instanceof MethodNode) {
				MethodNode methodNode = (MethodNode) o;
				AbstractInsnNode insn = methodNode.instructions.getFirst();

				while (insn != null) {
					String name = null;

					if (insn instanceof MethodInsnNode) {
						name = ((MethodInsnNode) insn).name;
					} else if (insn instanceof FieldInsnNode) {
						name = ((FieldInsnNode) insn).name;
					}

					if (name != null && cheatStrings.contains(name)) {
						System.out.println("Found possible mod cheats. " + name + " in " + className);
						possibleCheats++;
					}

					insn = insn.getNext();
				}
			}
		}

		return new ModClassSummary(possibleCheats, className);
	}

	private class ModSummary {
		private final int possibleCheats;
		private final List<String> classes;

		private ModSummary(int possibleCheats, List<String> classes) {
			this.possibleCheats = possibleCheats;
			this.classes = classes;
		}

		private String getMainClassPackage() {
			Map<String, Integer> mostCommon = new HashMap<>();

			for (String s : classes) {
				try {
					String[] packages = s.split("\\.");
					if (packages.length < 3)
						continue;

					StringBuilder leadingPackage = new StringBuilder();
					for (int i = 0; i < 3; i++) {
						leadingPackage.append(packages[i]);
						leadingPackage.append('.');
					}

					leadingPackage.deleteCharAt(leadingPackage.length() - 1);
					String leadingPackageStr = leadingPackage.toString();

					int amount = mostCommon.containsKey(leadingPackageStr) ? mostCommon.get(leadingPackageStr) : 0;
					amount++;
					mostCommon.put(leadingPackageStr, amount);
				} catch (Exception e) {
					/* Invalid class format */ }
			}

			int mostCommonFreq = -1;
			String mostCommonPackage = null;

			for (String pack : mostCommon.keySet()) {
				int freq = mostCommon.get(pack);
				if (freq > mostCommonFreq) {
					mostCommonFreq = freq;
					mostCommonPackage = pack;
				}
			}

			return mostCommonPackage;
		}
	}

	private class ModClassSummary {
		private final int possibleCheats;
		private final String className;

		public ModClassSummary(int possibleCheats, String className) {
			this.possibleCheats = possibleCheats;
			this.className = className;
		}
	}
}
