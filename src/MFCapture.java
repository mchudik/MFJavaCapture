import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class MFCapture {
	private MFCaptureLib MFLibrary;
	static final int DEVICE_AUDIO = 1;
	static final int DEVICE_VIDEO = 2;
	static final int DEVICE_MONITOR = 3;
	
	public interface MFCaptureLib extends Library {
		public boolean MFCaptureMFStartup();
	    public boolean MFCaptureMFShutdown();
	    public Pointer MFCaptureHRToString(long HRESULT);
	    public boolean MFCaptureCreateCapture(LongByReference hr);
	    public boolean MFCaptureAddDevice(int nDevType, WString devName, WString devUniqueId, WString devOutputFile, Pointer pDevActivate, LongByReference hr);
	    public boolean MFCaptureStartPreview(long hWnd1, long hWnd2, LongByReference hr);
	    public boolean MFCaptureStartRecord(long hWnd1, long hWnd2, LongByReference hr);
	    public boolean MFCaptureEndCapture(LongByReference hr);

	    public boolean MFDeviceEnumerateByType(int nDeviceType, IntByReference nDeviceCount, LongByReference hr);
	    public boolean MFDeviceGetName(int nDeviceIndex, PointerByReference wszDeviceName, LongByReference hr);
	    public boolean MFDeviceGetUniqueID(int nDeviceIndex, PointerByReference wszDeviceID, LongByReference hr);
	    public boolean MFDeviceGetActivate(int nDeviceIndex, PointerByReference pActivate, LongByReference hr);
	}
	   
	public MFCapture(){
		try {
			MFLibrary = (MFCaptureLib) Native.loadLibrary("MFCapture", MFCaptureLib.class);
			boolean bRetval = MFLibrary.MFCaptureMFStartup();
			System.out.println("MFDeviceMFStartup(): " + "Returned => " + bRetval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize( ) throws Throwable {
		boolean bRetval = MFLibrary.MFCaptureMFShutdown();
		System.out.println("MFDeviceMFShutdown(): " + "Returned => " + bRetval);
	}

	public String MFDeviceHRToString(long hr){
       try {
    	   Pointer pStrMsg = MFLibrary.MFCaptureHRToString(hr);
    	   char[] bufferHr = pStrMsg.getCharArray(0, 128);
    	   String normalString = Native.toString(bufferHr);
    	   return normalString;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	   
    public int MFDeviceEnumerateByType(int nDeviceType) {
    	try {
    		IntByReference nDeviceCount = new IntByReference();
    		LongByReference hr = new LongByReference();
    		boolean bRetval = MFLibrary.MFDeviceEnumerateByType(nDeviceType, nDeviceCount, hr);
    		if(!bRetval) {
    			System.out.println("MFDeviceEnumerateByType(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return 0;
    		}
    		return nDeviceCount.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    public String MFDeviceGetName(int nDeviceIndex) {
    	try {
    		PointerByReference wszDeviceName = new PointerByReference();
 		   	LongByReference hr = new LongByReference();
 		   	boolean bRetval = MFLibrary.MFDeviceGetName(nDeviceIndex, wszDeviceName, hr);
    		if(!bRetval) {
    			System.out.println("MFDeviceGetName(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return "";
    		}
 		   	Pointer p = wszDeviceName.getValue();
 		   	char[] buffer = p.getCharArray(0, 128);
 		   	String normalString = Native.toString(buffer);
 		   	return normalString;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
    public String MFDeviceGetUniqueID(int nDeviceIndex) {
    	try {
    		PointerByReference wszDeviceID = new PointerByReference();
 		   	LongByReference hr = new LongByReference();
 		   	boolean bRetval = MFLibrary.MFDeviceGetUniqueID(nDeviceIndex, wszDeviceID, hr);
    		if(!bRetval) {
    			System.out.println("MFDeviceGetUniqueID(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return "";
    		}
 		   	Pointer p = wszDeviceID.getValue();
 		   	char[] buffer = p.getCharArray(0, 256);
 		   	String normalString = Native.toString(buffer);
 		   	return normalString;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }

    public Pointer MFDeviceGetActivate(int nDeviceIndex) {
    	try {
    		PointerByReference pActivate = new PointerByReference();
 		   	LongByReference hr = new LongByReference();
 		   	boolean bRetval = MFLibrary.MFDeviceGetActivate(nDeviceIndex, pActivate, hr);
    		if(!bRetval) {
    			System.out.println("MFDeviceGetActivate(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return null;
    		}
    		return pActivate.getValue();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    public boolean MFCaptureCreateCapture() {
    	try {
    		LongByReference hr = new LongByReference();
    		boolean bRetval = MFLibrary.MFCaptureCreateCapture(hr);
    		if(!bRetval) {
    			System.out.println("MFCaptureCreateCapture(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return false;
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    public boolean MFCaptureAddDevice(int nDevType, String devName, String devUniqueId, String devOutputFile, Pointer pDevActivate) {
    	try {
    		LongByReference hr = new LongByReference();
			WString wstrdevName = new WString(devName);
			WString wstrDevUniqueId = new WString(devUniqueId);
			WString wstrdevOutput = new WString(devOutputFile);
    		boolean bRetval = MFLibrary.MFCaptureAddDevice(nDevType, wstrdevName, wstrDevUniqueId, wstrdevOutput, pDevActivate, hr);
    		if(!bRetval) {
    			System.out.println("MFCaptureAddDevice(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return false;
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
    
    public boolean MFCaptureStartPreview(long hWnd1, long hWnd2) {
    	try {
    		LongByReference hr = new LongByReference();
    		boolean bRetval = MFLibrary.MFCaptureStartPreview(hWnd1, hWnd2, hr);
    		if(!bRetval) {
    			System.out.println("MFCaptureStartPreview(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return false;
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    public boolean MFCaptureStartRecord(long hWnd1, long hWnd2) {
    	try {
    		LongByReference hr = new LongByReference();
    		boolean bRetval = MFLibrary.MFCaptureStartRecord(hWnd1, hWnd2, hr);
    		if(!bRetval) {
    			System.out.println("MFCaptureStartRecord(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return false;
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

    public boolean MFCaptureEndCapture() {
    	try {
    		LongByReference hr = new LongByReference();
    		boolean bRetval = MFLibrary.MFCaptureEndCapture(hr);
    		if(!bRetval) {
    			System.out.println("MFCaptureEndCapture(): " + "Returned => " + bRetval + ": HRESULT => " + MFDeviceHRToString(hr.getValue()));
    			return false;
    		}
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }

}