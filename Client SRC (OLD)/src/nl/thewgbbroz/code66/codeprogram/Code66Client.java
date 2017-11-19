package nl.thewgbbroz.code66.codeprogram;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import nl.thewgbbroz.code66.StringsCollection;
import nl.thewgbbroz.code66.utils.Utils;

public class Code66Client {
	private static final int CODE_LENGTH = 4;

	private Code66Client() {
	}

	public static ServerResult downloadMainProgramFile(Address server, byte[] code) throws IOException {
		if (code.length != CODE_LENGTH)
			throw new IllegalArgumentException("code.length must be " + CODE_LENGTH + "!");

		System.out.println("Connecting to " + server.toString() + "..");

		Socket socket = new Socket(server.ip, server.port);

		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		System.out.println("Connected! Giving server the code..");

		// Writing code to the server
		out.write(code, 0, CODE_LENGTH);

		// Writing bit to the server
		// out.write(Main.BIT);

		// Receiving (cut down) strings from server
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf;
		buf = new byte[1024];
		int read;
		while ((read = in.read(buf)) > 0) {
			baos.write(buf, 0, read);
		}

		// baos contains encrypted data
		byte[] decrypted = Utils.decrypt(baos.toByteArray(), "{FILE}" + new String(code));

		ByteArrayInputStream bais = new ByteArrayInputStream(decrypted);

		StringsCollection strings;
		try {
			strings = StringsCollection.deserialize(bais);
		} catch (EOFException e) {
			// No more data exists, server didn't send it. Invalid code.
			socket.close();
			System.out.println("No more data exists. Assuming an invalid code.");
			return null;
		}

		socket.close();

		System.out.println("Done.");

		return new ServerResult(strings);
	}

	public static class ServerResult {
		public final StringsCollection stringsCollection;

		public ServerResult(StringsCollection stringsCollection) {
			this.stringsCollection = stringsCollection;
		}
	}
}
