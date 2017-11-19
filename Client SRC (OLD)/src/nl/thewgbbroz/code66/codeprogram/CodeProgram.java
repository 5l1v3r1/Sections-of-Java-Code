package nl.thewgbbroz.code66.codeprogram;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import nl.thewgbbroz.code66.Main;
import nl.thewgbbroz.code66.codeprogram.Code66Client.ServerResult;
import nl.thewgbbroz.code66.mainprogram.MainProgram;

public class CodeProgram {
	private static final int WIDTH = 720;
	private static final int HEIGHT = 420;

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Text inpCode;
	private Image icon;

	private IPPicker ipPicker;

	public CodeProgram() {
		try {
			ipPicker = new IPPicker(Main.IPS_WEBSITE);
		} catch (IOException e) {
			Shell shell = new Shell();
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("Could not connect to server. You need a usable internet connection for this program to work. " + e.toString());
			dialog.open();

			shell.dispose();

			return;
		}

		System.out.println("Opening window..");
		open();
	}

	public void open() {
		icon = new Image(Display.getDefault(), getClass().getResourceAsStream("/icon.png"));

		Display display = Display.getDefault();

		createContents();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void createContents() {
		shell = new Shell(SWT.RESIZE | SWT.CLOSE | SWT.MIN);
		shell.setSize(WIDTH, HEIGHT);
		shell.setText("Code 66");
		shell.setLayout(null);
		shell.setBackgroundImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/background.png")));
		shell.setImage(icon);

		Label lblCopyright = new Label(shell, SWT.NONE);
		lblCopyright.setText("Copyright © Code 66 2017");
		lblCopyright.setForeground(new Color(Display.getDefault(), 255, 255, 255));
		lblCopyright.setBackground(new Color(Display.getDefault(), 0, 0, 0, 0));
		lblCopyright.setBounds(20, 340, 300, 30);
		lblCopyright.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.NORMAL));

		Label lblPin = new Label(shell, SWT.NONE);
		lblPin.setForeground(new Color(Display.getDefault(), 255, 255, 255));
		lblPin.setText("PIN:");
		lblPin.setBackground(new Color(Display.getDefault(), 0, 0, 0, 0));
		lblPin.setBounds(349, 210, 30, 20);

		inpCode = new Text(shell, SWT.BORDER | SWT.CENTER);
		inpCode.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		inpCode.setBounds(WIDTH / 2 - 50 / 2, 230, 50, 25);

		Label lblIcon = new Label(shell, SWT.NONE);
		lblIcon.setImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/logo_square_full.png")));
		lblIcon.setBounds(265, 10, 200, 200);
		lblIcon.setBackground(new Color(Display.getDefault(), 0, 0, 0, 0));

		Button btnSubmit = formToolkit.createButton(shell, "Login", SWT.NONE);
		btnSubmit.setBounds(WIDTH / 2 - 75 / 2, 300, 75, 25);

		inpCode.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int num = (int) e.character - 48;
				if (num >= 0 && num <= 9) {
					if (inpCode.getText().length() >= Main.CODE_LENGTH)
						e.doit = false;
					return;
				}

				if (!(e.keyCode == SWT.BS || e.keyCode == SWT.DEL || e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT)) {
					e.doit = false;
				}

				if (e.keyCode == SWT.CR || e.keyCode == SWT.LF) {
					e.doit = false;

					submitPressed();
				}
			}
		});

		btnSubmit.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				submitPressed();
			}
		});
	}

	private void submitPressed() {
		String codeStr = inpCode.getText();
		if (codeStr.length() != Main.CODE_LENGTH) {
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Authentication");
			dialog.setMessage("Initialization Failed!");
			dialog.open();
			return;
		}

		byte[] code = toCode(codeStr);

		ServerResult res;
		try {
//			res = Code66Client.downloadMainProgramFile(new Address("localhost", 6443), code);
			res = Code66Client.downloadMainProgramFile(ipPicker.getRandomAddress(), code);
		} catch (IOException e1) {
			e1.printStackTrace();

			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Authentication");
			dialog.setMessage("Initialization Failed!");
			dialog.open();
			return;
		}

		if (res == null) {
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Authentication");
			dialog.setMessage("Initialization Failed!");
			dialog.open();
			return;
		}

		// executeJarFile(res.downloaded, stringsFile.getAbsolutePath(), false, true);
		Display.getDefault().dispose();

		new MainProgram(res.stringsCollection);
	}

	private byte[] toCode(String codeStr) {
		if (codeStr.length() != Main.CODE_LENGTH)
			throw new IllegalArgumentException("Code length must be " + Main.CODE_LENGTH);

		byte[] code = new byte[Main.CODE_LENGTH];

		for (int i = 0; i < Main.CODE_LENGTH; i++) {
			char c = codeStr.charAt(i);
			int num = (int) c - 48;
			code[i] = (byte) num;
		}

		return code;
	}
}
