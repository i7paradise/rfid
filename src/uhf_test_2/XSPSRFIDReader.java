/**
 * 
 */
package uhf_test_2;

import java.util.Vector;

import com.rfidread.RFIDReader;
import com.rfidread.Tag6C;
import com.rfidread.Enumeration.eAntennaNo;
import com.rfidread.Enumeration.eReadType;
import com.rfidread.Models.Tag_Model;

/**
 * @author hmr
 *
 */
public class XSPSRFIDReader implements Runnable {

	private String readerIP;
	private int readerPort;
	private String ConnId;
	private RFIDReaderProducer rfidReaderProducer = null;
	private transient String brokerURL = "tcp://localhost:61616";
	private String brokerStorePrefix = null;
	private Vector<Integer> antennaSet = null;

	/**
	 * @param readerIP
	 * @param readerPort
	 */
	public XSPSRFIDReader(String readerIP, int readerPort, String brokerStorePrefix,
			Vector<Integer> antennaSet) {
		this.readerIP = readerIP;
		this.readerPort = readerPort;
		this.ConnId = readerIP + ":" + String.valueOf(this.readerPort);
		this.antennaSet = antennaSet;
		this.brokerStorePrefix = new String(brokerStorePrefix);
	}

	
	public String getReaderIP() {
		return readerIP;
	}


	public void setReaderIP(String readerIP) {
		this.readerIP = readerIP;
	}


	public int getReaderPort() {
		return readerPort;
	}


	public void setReaderPort(int readerPort) {
		this.readerPort = readerPort;
	}


	public String getBrokerURL() {
		return brokerURL;
	}


	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}


	boolean connectRFIDReader() {
		try {
			rfidReaderProducer = new RFIDReaderProducer(brokerURL, brokerStorePrefix, antennaSet);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		Tag_Model wmsTag = null;
		byte[] param = new byte[] {(byte)0xe0, 0x4f, (byte)0xd0,
				0x20, (byte)0xea, 0x3a, 0x69, 0x10, (byte)0xa2, (byte)0xd8, 0x08, 0x00, 0x2b,
				0x30, 0x30, (byte)0x9d};
		try {
			wmsTag = new Tag_Model(param, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int cnt = 1; cnt < 11; cnt++) {
			wmsTag._ANT_NUM = cnt % 4;
			if (wmsTag._ANT_NUM == 0)
				wmsTag._ANT_NUM = 4;
			wmsTag._EPC = String.valueOf(cnt);
			rfidReaderProducer.OutPutTags(wmsTag);
		}

/*		if (RFIDReader.CreateTcpConn(ConnId, rfidReaderProducer)) {
			System.out.println("Connect success!\n");
			try {
				System.out.println("Thread " + Thread.currentThread().getId() + " Config: " + RFIDReader._Config.GetEPCBaseBandParam(ConnId));
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
			RFIDReader.Stop(ConnId);
		} else {
			System.out.println("Connect failure!\n");
		}*/
		return true;
	}

	void stopRFIDReader() {
		RFIDReader.Stop(ConnId);
	}

	void closeRFIDReader() {
		RFIDReader.CloseConn(ConnId);
	}

	@Override
	public void run() {
		int rt;

		if (connectRFIDReader() == false)
			return;

		int antennaSet = eAntennaNo._1.GetNum() | eAntennaNo._2.GetNum() | eAntennaNo._3.GetNum() | eAntennaNo._4.GetNum();
		rt = Tag6C.GetEPC(ConnId, antennaSet, eReadType.Inventory);
		if (rt == 0) {
			System.out.println("XSPSRFIDReader Success");
		} else {
			System.out.println("XSPSRFIDReader Failed");
		}

		try {
			Thread.sleep(100000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

}
