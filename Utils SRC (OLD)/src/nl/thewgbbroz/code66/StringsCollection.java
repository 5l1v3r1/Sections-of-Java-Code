package nl.thewgbbroz.code66;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringsCollection {
	public final List<CheatStrings> clientStrings;
	public final CheatStrings csrssStrings;
	public final CheatStrings explorerStrings;
	public final CheatStrings dwmStrings;

	public StringsCollection(List<CheatStrings> clientStrings, CheatStrings csrssStrings, CheatStrings explorerStrings, CheatStrings dwmStrings) {
		this.clientStrings = clientStrings;
		this.csrssStrings = csrssStrings;
		this.explorerStrings = explorerStrings;
		this.dwmStrings = dwmStrings;
	}

	public void serialize(OutputStream out) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);

		dos.writeInt(clientStrings.size());
		for (CheatStrings cs : clientStrings) {
			cs.serialize(dos);
		}

		csrssStrings.serialize(dos);

		explorerStrings.serialize(dos);
		
		dwmStrings.serialize(dos);
	}

	public StringsCollection getSome(int maxStrings) {
		Random rnd = new Random();

		List<CheatStrings> newClientStrings = new ArrayList<>();
		for (CheatStrings cs : clientStrings) {
			CheatStrings cutDownVersion = cs.getSome(maxStrings, rnd);
			newClientStrings.add(cutDownVersion);
		}

		CheatStrings newCsrss = csrssStrings.getSome(maxStrings, rnd);
		CheatStrings newExplorer = explorerStrings.getSome(maxStrings, rnd);
		CheatStrings newDwm = dwmStrings.getSome(maxStrings, rnd);

		return new StringsCollection(newClientStrings, newCsrss, newExplorer, newDwm);
	}

	public static StringsCollection deserialize(InputStream in) throws IOException {
		DataInputStream dis = new DataInputStream(in);

		List<CheatStrings> clientStrings = new ArrayList<>();
		int clientStringsAmount = dis.readInt();
		for (int i = 0; i < clientStringsAmount; i++) {
			clientStrings.add(CheatStrings.deserialize(dis));
		}

		CheatStrings csrssStrings = CheatStrings.deserialize(dis);

		CheatStrings explorerStrings = CheatStrings.deserialize(dis);

		CheatStrings dwmStrings = CheatStrings.deserialize(dis);

		return new StringsCollection(clientStrings, csrssStrings, explorerStrings, dwmStrings);
	}
}
