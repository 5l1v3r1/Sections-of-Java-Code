package nl.thewgbbroz.code66.mainprogram;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import nl.thewgbbroz.code66.StringsCollection;
import nl.thewgbbroz.code66.mainprogram.cheatdetector.CheatDetector;
import nl.thewgbbroz.code66.mainprogram.detections.DetectionResult;
import nl.thewgbbroz.code66.mainprogram.detections.Detector;
import nl.thewgbbroz.code66.mainprogram.detections.FileDetector;
import nl.thewgbbroz.code66.mainprogram.detections.ModDetector;
import nl.thewgbbroz.code66.mainprogram.detections.RecycleBinDetector;
import nl.thewgbbroz.code66.mainprogram.detections.ResourcePackDetector;
import nl.thewgbbroz.code66.mainprogram.detections.RunningProcessDetector;

public class MainProgram {
	private static final int WIDTH = 720;
	private static final int HEIGHT = 420;

	private static final int FILE_JAVAW = 0;
	private static final int FILE_CSRSS = 1;
	private static final int FILE_EXPLORER = 2;
	private static final int FILE_DWM = 3;

	public static final String PROCESSING = "Processing..";

	private static MainProgram _instance;

	protected Shell shell;

	private Label lblJavawDump, lblCsrssDump, lblExplorerDump, lblDwmDump;

	private File[] selectedFiles = new File[4];
	private CheatDetector cheatDetector;
	private Label lblStatus;

	private List<Detector> detectors = new ArrayList<>();

	public MainProgram(StringsCollection stringsCollection) {
		System.out.println("Opening main window..");

		_instance = this;

		cheatDetector = new CheatDetector(stringsCollection);

		open();
	}

	public void open() {
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
		shell.setImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/icon.png")));

		////////////////////

		Button btnJavawDump = new Button(shell, SWT.NONE);
		btnJavawDump.setBounds(11, 255, 100, 25);
		btnJavawDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectFile(lblJavawDump, FILE_JAVAW);
			}
		});
		btnJavawDump.setText("javaw.exe");
		btnJavawDump.setImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/javaw.exe.png")));

		lblJavawDump = new Label(shell, SWT.NONE);
		lblJavawDump.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblJavawDump.setBounds(116, 260, 180, 15);
		lblJavawDump.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblJavawDump.setText("None");

		////////////////////

		Button btnCsrssDump = new Button(shell, SWT.NONE);
		btnCsrssDump.setBounds(11, 285, 100, 25);
		btnCsrssDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectFile(lblCsrssDump, FILE_CSRSS);
			}
		});
		btnCsrssDump.setText("csrss.exe");
		btnCsrssDump.setImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/csrss.exe.png")));

		lblCsrssDump = new Label(shell, SWT.NONE);
		lblCsrssDump.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblCsrssDump.setBounds(116, 290, 180, 15);
		lblCsrssDump.setText("None");
		lblCsrssDump.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		////////////////////

		Button btnExplorerDump = new Button(shell, SWT.NONE);
		btnExplorerDump.setBounds(11, 315, 100, 25);
		btnExplorerDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectFile(lblExplorerDump, FILE_EXPLORER);
			}
		});
		btnExplorerDump.setText("explorer.exe");
		btnExplorerDump.setImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/explorer.exe.png")));

		lblExplorerDump = new Label(shell, SWT.NONE);
		lblExplorerDump.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblExplorerDump.setBounds(116, 320, 180, 15);
		lblExplorerDump.setText("None");
		lblExplorerDump.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		////////////////////

		Button btnDwmDump = new Button(shell, SWT.NONE);
		btnDwmDump.setBounds(11, 345, 100, 25);
		btnDwmDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectFile(lblDwmDump, FILE_DWM);
			}
		});
		btnDwmDump.setText("dwm.exe");
		btnDwmDump.setImage(new Image(Display.getDefault(), getClass().getResourceAsStream("/csrss.exe.png")));

		lblDwmDump = new Label(shell, SWT.NONE);
		lblDwmDump.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDwmDump.setBounds(116, 350, 180, 15);
		lblDwmDump.setText("None");
		lblDwmDump.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		////////////////////

		Button btnProcess = new Button(shell, SWT.NONE);
		btnProcess.setBounds(325, 315, 52, 25);
		btnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				startDetectionProcess();
			}
		});
		btnProcess.setText("Process");

		lblStatus = new Label(shell, SWT.NONE);
		lblStatus.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblStatus.setText("Waiting");
		lblStatus.setAlignment(SWT.RIGHT);
		lblStatus.setBounds(500, 356, 194, 15);
		lblStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));

		// 48 / 2 = 24
		// 720 - 48 * 4 = 528
		// 528 / 4 = 132

		detectors.add(new FileDetector(shell, 132, 60, "Check 1", SWTResourceManager.getColor(255, 255, 255), cheatDetector, FileDetector.CLIENTS));
		detectors.add(new FileDetector(shell, 264, 60, "Check 2", SWTResourceManager.getColor(0, 0, 0), cheatDetector, FileDetector.CSRSS));
		detectors.add(new FileDetector(shell, 396, 60, "Check 3", SWTResourceManager.getColor(0, 0, 0), cheatDetector, FileDetector.EXPLORER));
		detectors.add(new FileDetector(shell, 528, 60, "Check 4", SWTResourceManager.getColor(255, 255, 255), cheatDetector, FileDetector.DWM));

		detectors.add(new ModDetector(shell, 132, 165, "Check 5", SWTResourceManager.getColor(255, 255, 255), "/mod-cheat-strings.txt"));
		detectors.add(new RunningProcessDetector(shell, 264, 165, "Check 6", SWTResourceManager.getColor(255, 255, 255), "/recording-software.txt"));
		detectors.add(new RecycleBinDetector(shell, 396, 165, "Check 7", SWTResourceManager.getColor(255, 255, 255)));
		detectors.add(new ResourcePackDetector(shell, 528, 165, "Check 8", SWTResourceManager.getColor(255, 255, 255)));
	}

	private void selectFile(Label label, int fileId) {
		FileDialog fileDialog = new FileDialog(shell);
		String fileName = fileDialog.open();
		System.out.println("User selected " + fileName);

		if (fileName == null) {
			return;
		}

		File file = new File(fileName);

		boolean alreadySelected = false;
		for (int i = 0; i < selectedFiles.length; i++) {
			if (i != fileId && selectedFiles[i] != null && selectedFiles[i].equals(file)) {
				alreadySelected = true;
				break;
			}
		}

		if (alreadySelected) {
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("You've already selected this file before!");
			dialog.open();

			return;
		}

		label.setText(file.getName());

		selectedFiles[fileId] = file;
	}

	private void startDetectionProcess() {
		for (Detector det : detectors) {
			det.setCheckmark(Detector.GRAY);
		}

		new Thread(new Runnable() {
			public void run() {
				System.out.println("Starting to process files..");
				setStatus(PROCESSING);

				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_WAIT));
						shell.setEnabled(false);
					}
				});

				try {
					DetectionResult[] results = new DetectionResult[detectors.size()];

					for (int i = 0; i < detectors.size(); i++) {
						Detector detector = detectors.get(i);
						if (detector instanceof FileDetector && i < selectedFiles.length) {
							((FileDetector) detector).setFile(selectedFiles[i]);
						}

						results[i] = detector.getDetectionResult();
					}

					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							StringBuilder res = new StringBuilder();

							for (int i = 0; i < results.length; i++) {
								res.append("Check " + (i + 1) + ": " + results[i].getMessageString());
								res.append("\n\n");
							}

							res.delete(res.length() - 2, res.length());

							MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
							dialog.setText("Initialization Complete");
							dialog.setMessage(res.toString());
							dialog.open();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();

					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Error");
							dialog.setMessage("Something went wrong while processing a file! " + e.toString());
							dialog.open();
						}
					});
				}

				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						shell.setEnabled(true);
						shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_ARROW));
					}
				});

				System.out.println("Done processing files.");
				setStatus("Done");

				try {
					((Closeable) getClass().getClassLoader()).close();
					new File(MainProgram.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).delete();
				} catch (IOException | URISyntaxException e) {
					System.out.println("Could not self destruct file!");
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void setStatus(String status) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				_instance.lblStatus.setText(status);
			}
		});
	}
}
