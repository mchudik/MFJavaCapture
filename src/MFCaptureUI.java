import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;


public class MFCaptureUI {

	protected Shell shell;
	private static MFCapture MFDeviceLib;
	private Text txtCaptureFile;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("JVM => " + System.getProperty("sun.arch.data.model"));
			MFCaptureUI window = new MFCaptureUI();
			MFDeviceLib = new MFCapture();
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
		shell.setSize(640, 477);
		shell.setText("MFJavaCapture SWT Application");
		
		final Combo comboAudio = new Combo(shell, SWT.READ_ONLY);
		comboAudio.setBounds(20, 36, 581, 28);
		
		final Combo comboVideo = new Combo(shell, SWT.READ_ONLY);
		comboVideo.setBounds(316, 299, 285, 28);
		
		final Combo comboDesktop = new Combo(shell, SWT.READ_ONLY);
		comboDesktop.setBounds(20, 299, 285, 28);
		
		final Button btnRecord = new Button(shell, SWT.NONE);
		btnRecord.setBounds(71, 384, 196, 30);
		btnRecord.setText("Record");
		
		Button btnPlay = new Button(shell, SWT.NONE);
		btnPlay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Runtime rs = Runtime.getRuntime();
					rs.exec("C:\\Program Files (x86)\\Windows Media Player\\wmplayer.exe " + txtCaptureFile.getText());
				}catch(Exception exc)
				{
					MessageDialog.openError(shell, "Error", "Bad Error");
				}
			}
		});
		btnPlay.setText("Play");
		btnPlay.setBounds(367, 384, 196, 30);
		
		final Canvas canvasDesktop = new Canvas(shell, SWT.BORDER);
		canvasDesktop.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		canvasDesktop.setBounds(20, 96, 285, 204);
		
		final Canvas canvasVideo = new Canvas(shell, SWT.BORDER);
		canvasVideo.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		canvasVideo.setBounds(316, 96, 285, 204);

		final Button btnAudio = new Button(shell, SWT.CHECK);
		btnAudio.setSelection(true);
		btnAudio.setBounds(20, 10, 111, 20);
		btnAudio.setText("Audio");
		
		final Button btnDesktop = new Button(shell, SWT.CHECK);
		btnDesktop.setSelection(true);
		btnDesktop.setBounds(20, 70, 111, 20);
		btnDesktop.setText("Desktop");
		
		final Button btnVideo = new Button(shell, SWT.CHECK);
		btnVideo.setSelection(true);
		btnVideo.setBounds(316, 70, 111, 20);
		btnVideo.setText("Video");

		txtCaptureFile = new Text(shell, SWT.BORDER);
		txtCaptureFile.setText("C:\\Temp\\Output.wmv");
		txtCaptureFile.setBounds(108, 339, 493, 26);
		
		Label lblCaptureFile = new Label(shell, SWT.NONE);
		lblCaptureFile.setBounds(20, 342, 82, 20);
		lblCaptureFile.setText("Capture File:");

		btnRecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
					if(btnRecord.getText() == "Stop") {
						if(MFDeviceLib.MFCaptureEndCapture()) {
							System.out.println("MFCaptureEndCapture(): => Success");
						}
						canvasDesktop.redraw();
						canvasVideo.redraw();
						btnRecord.setText("Record");
					} else {
						if(!MFDeviceLib.MFCaptureCreateCapture())
							return;
		    			System.out.println("\nMFCaptureCreateCapture(): => Success");
		    			
		    			// Set Selected Audio Device
		    			if(btnAudio.getSelection() ) {
			    			if(!SetSelectedDevice(MFCapture.DEVICE_AUDIO, comboAudio.getSelectionIndex(), txtCaptureFile.getText())) {
								System.out.println("SetSelectedDevice(MFCapture.DEVICE_AUDIO): => Error");
			    				return;
			    			}
		    			}
	    			
		    			// Set Selected Video Device
		    			if(btnVideo.getSelection() ) {
			    			if(!SetSelectedDevice(MFCapture.DEVICE_VIDEO, comboVideo.getSelectionIndex(), txtCaptureFile.getText())) {
								System.out.println("SetSelectedDevice(MFCapture.DEVICE_VIDEO): => Error");
			    				return;
			    			}
		    			}
		    			
		    			// Set Selected Display Device
		    			if(btnDesktop.getSelection() ) {
			    			if(!SetSelectedDevice(MFCapture.DEVICE_MONITOR, comboDesktop.getSelectionIndex(), txtCaptureFile.getText())) {
								System.out.println("SetSelectedDevice(MFCapture.DEVICE_MONITOR): => Error");
			    				return;
			    			}
		    			}
		    			
		    			//Start Capture
		        		long hWnd1 = canvasDesktop.handle;
		        		long hWnd2 = canvasVideo.handle;
						if(!MFDeviceLib.MFCaptureStartRecord(hWnd1, hWnd2)) {
			    			System.out.println("MFCaptureStartRecord(): => Error");
							return;
						}
						System.out.println("MFCaptureStartRecord(): => Success");
						btnRecord.setText("Stop");
					}
				}catch(Exception exc)
				{
					MessageDialog.openError(shell, "Error", "Bad Error");
				}
			}
		});
		initializeContents(comboAudio, comboVideo, comboDesktop);
		
	}

	public void initializeContents(Combo comboAudio, Combo comboVideo, Combo comboDesktop) {
		try{
			comboAudio.removeAll();
			comboVideo.removeAll();
			comboDesktop.removeAll();

			// Retrieve Audio Devices
			int nDevices = MFDeviceLib.MFDeviceEnumerateByType(MFCapture.DEVICE_AUDIO);
			System.out.println("\nAudio Devices Found => " + nDevices);
			for(int i=0; i<nDevices; i++) {
				comboAudio.add(MFDeviceLib.MFDeviceGetName(i));
				System.out.println(i + " => " + MFDeviceLib.MFDeviceGetName(i) + " => " + MFDeviceLib.MFDeviceGetUniqueID(i) + " => " + MFDeviceLib.MFDeviceGetActivate(i));
			}
			
			// Retrieve Video Devices
			nDevices = MFDeviceLib.MFDeviceEnumerateByType(MFCapture.DEVICE_VIDEO);
			System.out.println("\nVideo Devices Found => " + nDevices);
			for(int i=0; i<nDevices; i++) {
				comboVideo.add(MFDeviceLib.MFDeviceGetName(i));
				System.out.println(i + " => " + MFDeviceLib.MFDeviceGetName(i) + " => " + MFDeviceLib.MFDeviceGetUniqueID(i) + " => " + MFDeviceLib.MFDeviceGetUniqueID(i));
			}

			// Retrieve Monitor Devices
			nDevices = MFDeviceLib.MFDeviceEnumerateByType(MFCapture.DEVICE_MONITOR);
			System.out.println("\nMonitor Devices Found => " + nDevices);
			for(int i=0; i<nDevices; i++) {
				comboDesktop.add(MFDeviceLib.MFDeviceGetName(i));
				System.out.println(i + " => " + MFDeviceLib.MFDeviceGetName(i) + " => " + MFDeviceLib.MFDeviceGetUniqueID(i) + " => " + MFDeviceLib.MFDeviceGetUniqueID(i));
			}

			comboVideo.select(0);
			comboAudio.select(0);
			comboDesktop.select(0);
		}catch(Exception exc)
		{
			MessageDialog.openError(shell, "Error", "Bad Error");
		}
	}
	public boolean SetSelectedDevice(int nDevType, int nIndex, String devOutputFile) {
		try {
			// Set Selected Device
			int nDevices = MFDeviceLib.MFDeviceEnumerateByType(nDevType);
			if((nIndex >= 0) && (nIndex < nDevices)) {
				System.out.println("Adding => " + nIndex + " => " + MFDeviceLib.MFDeviceGetName(nIndex) + " => " + MFDeviceLib.MFDeviceGetUniqueID(nIndex) + " => " + MFDeviceLib.MFDeviceGetActivate(nIndex));
				if(MFDeviceLib.MFCaptureAddDevice(nDevType, MFDeviceLib.MFDeviceGetName(nIndex), MFDeviceLib.MFDeviceGetUniqueID(nIndex), devOutputFile, MFDeviceLib.MFDeviceGetActivate(nIndex)))
					return true;	
			}
			return false;
		}catch(Exception exc)
		{
			MessageDialog.openError(shell, "Error", "Bad Error");
			return false;
		}
	}
}
