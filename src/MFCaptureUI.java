import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


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
		shell.setSize(640, 480);
		shell.setText("MFJavaCapture SWT Application");
		
		final Combo comboAudio = new Combo(shell, SWT.READ_ONLY);
		comboAudio.setBounds(47, 58, 285, 28);
		
		Label lblAudio = new Label(shell, SWT.NONE);
		lblAudio.setBounds(47, 32, 70, 20);
		lblAudio.setText("Audio");
		
		Label lblVideo = new Label(shell, SWT.NONE);
		lblVideo.setText("Video");
		lblVideo.setBounds(47, 115, 70, 20);
		
		final Combo comboVideo = new Combo(shell, SWT.READ_ONLY);
		comboVideo.setBounds(47, 141, 285, 28);
		
		Label lblDesktop = new Label(shell, SWT.NONE);
		lblDesktop.setText("Desktop");
		lblDesktop.setBounds(47, 199, 70, 20);
		
		final Combo comboDesktop = new Combo(shell, SWT.READ_ONLY);
		comboDesktop.setBounds(47, 225, 285, 28);
		
		Button btnRecord = new Button(shell, SWT.NONE);
		btnRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
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
		});
		btnRecord.setBounds(101, 379, 196, 30);
		btnRecord.setText("Record");
		
		Button btnPlay = new Button(shell, SWT.NONE);
		btnPlay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnPlay.setText("Play");
		btnPlay.setBounds(339, 379, 196, 30);

	}
}
