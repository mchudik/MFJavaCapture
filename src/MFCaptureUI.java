import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.wb.swt.SWTResourceManager;


public class MFCaptureUI {

	protected Shell shell;
	private static MFDevice MFDeviceLib;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("JVM => " + System.getProperty("sun.arch.data.model"));
			MFCaptureUI window = new MFCaptureUI();
			MFDeviceLib = new MFDevice();
			window.open();
			System.runFinalization(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(640, 446);
		shell.setText("MFJavaCapture SWT Application");
		
		final Combo comboAudio = new Combo(shell, SWT.READ_ONLY);
		comboAudio.setBounds(20, 36, 581, 28);
		
		Label lblAudio = new Label(shell, SWT.NONE);
		lblAudio.setBounds(20, 10, 70, 20);
		lblAudio.setText("Audio");
		
		Label lblVideo = new Label(shell, SWT.NONE);
		lblVideo.setText("Video");
		lblVideo.setBounds(316, 70, 70, 20);
		
		final Combo comboVideo = new Combo(shell, SWT.READ_ONLY);
		comboVideo.setBounds(316, 299, 285, 28);
		
		Label lblDesktop = new Label(shell, SWT.NONE);
		lblDesktop.setText("Desktop");
		lblDesktop.setBounds(20, 70, 70, 20);
		
		final Combo comboDesktop = new Combo(shell, SWT.READ_ONLY);
		comboDesktop.setBounds(20, 299, 285, 28);
		
		Button btnRecord = new Button(shell, SWT.NONE);
		btnRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{

				}catch(Exception exc)
				{
					MessageDialog.openError(shell, "Error", "Bad Error");
				}
			}
		});
		btnRecord.setBounds(68, 352, 196, 30);
		btnRecord.setText("Record");
		
		Button btnPlay = new Button(shell, SWT.NONE);
		btnPlay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnPlay.setText("Play");
		btnPlay.setBounds(364, 352, 196, 30);
		
		Canvas canvasDesktop = new Canvas(shell, SWT.BORDER);
		canvasDesktop.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		canvasDesktop.setBounds(20, 96, 285, 204);
		
		Canvas canvasVideo = new Canvas(shell, SWT.BORDER);
		canvasVideo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		canvasVideo.setBounds(316, 96, 285, 204);

		initializeContents(comboAudio, comboVideo, comboDesktop);
	}

	public void initializeContents(Combo comboAudio, Combo comboVideo, Combo comboDesktop) {
		try{
			comboAudio.removeAll();
			comboVideo.removeAll();
			comboDesktop.removeAll();

			// Retrieve Audio Devices
			int nDevices = MFDeviceLib.MFDeviceEnumerateByType(MFDevice.DEVICE_AUDIO);
			System.out.println("\nAudio Devices Found => " + nDevices);
			for(int i=0; i<nDevices; i++) {
				comboAudio.add(MFDeviceLib.MFDeviceGetName(i));
				System.out.println(i + " => " + MFDeviceLib.MFDeviceGetName(i));
			}
			
			// Retrieve Video Devices
			nDevices = MFDeviceLib.MFDeviceEnumerateByType(MFDevice.DEVICE_VIDEO);
			System.out.println("\nVideo Devices Found => " + nDevices);
			for(int i=0; i<nDevices; i++) {
				comboVideo.add(MFDeviceLib.MFDeviceGetName(i));
				System.out.println(i + " => " + MFDeviceLib.MFDeviceGetName(i));
			}

			// Retrieve Monitor Devices
			nDevices = MFDeviceLib.MFDeviceEnumerateByType(MFDevice.DEVICE_MONITOR);
			System.out.println("\nMonitor Devices Found => " + nDevices);
			for(int i=0; i<nDevices; i++) {
				comboDesktop.add(MFDeviceLib.MFDeviceGetName(i));
				System.out.println(i + " => " + MFDeviceLib.MFDeviceGetName(i));
			}

			comboVideo.select(0);
			comboAudio.select(0);
			comboDesktop.select(0);
		}catch(Exception exc)
		{
			MessageDialog.openError(shell, "Error", "Bad Error");
		}
	}
}
