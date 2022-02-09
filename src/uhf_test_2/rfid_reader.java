/**
 * 
 */
package uhf_test_2;

import com.rfidread.Interface.IAsynchronousMessage;
import com.rfidread.Interface.ISearchDevice;
import com.rfidread.Models.Device_Model;
import com.rfidread.Models.GPI_Model;
import com.rfidread.Models.Tag_Model;
import uhf_test_2.rfid_xsps;

/**
 * @author hmr
 *
 */
public class rfid_reader implements IAsynchronousMessage, ISearchDevice {
	@Override
	public void GPIControlMsg(GPI_Model arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS GPIControlMsg: " + arg0.ReaderName);		
	}

	@Override
	public void OutPutTags(Tag_Model tag) {
		// TODO Auto-generated method stub
//		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS Antena: " + tag._ANT_NUM + " -> X-SPS EPC: " + tag._EPC + " - TID:" + tag._TID);
		rfid_xsps.read_cnt++;
//		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS Antena: " + tag._ANT_NUM);
//		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS Antena: " + tag._ANT_NUM +  " - Data: " + tag._UserData);
	}

	@Override
	public void OutPutTagsOver() {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS OutPutTagsOver: ");		
	}

	@Override
	public void PortClosing(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS PortClosing: " + arg0);
	}

	@Override
	public void PortConnecting(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS PortConnecting: " + arg0);
	}

	@Override
	public void WriteDebugMsg(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS WriteDebugMsg: " + arg0);
	}

	@Override
	public void WriteLog(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS WriteLog: " + arg0);
	}

	@Override
	public void DebugMsg(String arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS DebugMsg: " + arg0);
	}

	@Override
	public void DeviceInfo(Device_Model arg0) {
		// TODO Auto-generated method stub
		System.out.println("Thread " + Thread.currentThread().getId() + " -> X-SPS DeviceInfo: " + arg0._ConnectMode + " | " + arg0._IP);
	}
}
