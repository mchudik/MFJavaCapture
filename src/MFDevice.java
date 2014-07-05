import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;

public class MFDevice {
	private MFDeviceLib MFLibrary;
	static final int DEVICE_AUDIO = 1;
	static final int DEVICE_VIDEO = 2;
	static final int DEVICE_MONITOR = 3;
	
	public interface MFDeviceLib extends Library {
		public boolean MFDeviceMFStartup();
	    public boolean MFDeviceMFShutdown();
	    public Pointer MFDeviceHRToString(long HRESULT);
	    public boolean MFDeviceEnumerateByType(int nDeviceType, IntByReference nDeviceCount, LongByReference hr);
	    public boolean MFDeviceGetName(int nDeviceIndex, PointerByReference wszDeviceName, LongByReference hr);
	}
	   
	public MFDevice(){
		try {
			MFLibrary = (MFDeviceLib) Native.loadLibrary("MFDevice", MFDeviceLib.class);
			boolean bRetval = MFLibrary.MFDeviceMFStartup();
			System.out.println("MFDeviceMFStartup(): " + "Returned => " + bRetval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize( ) throws Throwable {
		boolean bRetval = MFLibrary.MFDeviceMFShutdown();
		System.out.println("MFDeviceMFShutdown(): " + "Returned => " + bRetval);
	}

	public String MFDeviceHRToString(long hr){
       try {
    	   Pointer pStrMsg = MFLibrary.MFDeviceHRToString(hr);
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
}