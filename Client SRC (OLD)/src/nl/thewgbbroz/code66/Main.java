package nl.thewgbbroz.code66;

import java.util.Random;

import javax.swing.JOptionPane;

import nl.thewgbbroz.code66.codeprogram.CodeProgram;

public class Main {
	public static final Random RAND = new Random();
	public static final int CODE_LENGTH = 4;
	public static final String IPS_WEBSITE = "http://code66.zone/ips.txt";

	public static void main(String[] args) {
		try {
			new CodeProgram();

			// Using this will cause an error when trying to look for strings inside files!
			// new MainProgram(new StringsCollection(new ArrayList<>(), new CheatStrings("",
			// new ArrayList<>()), new CheatStrings("", new ArrayList<>()), new
			// CheatStrings("", new ArrayList<>())));
		} catch (UnsatisfiedLinkError e) {
			// This is a 64 bit program and it crashed
			JOptionPane.showMessageDialog(null, "You appear to be running a 32 bit Java JVM! Please use the 32 bit version of this program.", "Error", JOptionPane.ERROR_MESSAGE);

			e.printStackTrace();
		}
	}
}