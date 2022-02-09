package uhf_test_2;

import com.jamierf.rxtx.RXTXLoader;

import com.rfidread.Interface.IAsynchronousMessage;
import com.rfidread.Interface.ISearchDevice;
import com.rfidread.Models.Device_Model;
import com.rfidread.Models.GPI_Model;
import com.rfidread.Models.Tag_Model;
import com.rfidread.Enumeration.*;
import com.rfidread.RFIDReader;
import com.rfidread.*;
//import javafx.application.Application;

import java.util.HashMap;
import java.util.Scanner;

class rfid_task extends Thread{
	int task;

	public rfid_task(int itask) {
		task = itask;
	}

	public void run() {
		int rt;
		String match_epc = new String("EB0CAF");
		String epc_data = new String("393132333435");
		String epc_wms = new String("EB0CAF77884512D699000017");
//		rt = RFIDReader._Tag6C.GetEPC_MatchEPC(ConnID, eAntennaNo._1.GetNum(), eReadType.Inventory, match_epc);
/*		if (task == 1)
			rt = RFIDReader._Tag6C.GetEPC_TID_UserData(rfid_xsps.ConnID, eAntennaNo._1.GetNum(), eReadType.Inventory, 0, 12);
		else
			rt = RFIDReader._Tag6C.GetEPC_TID_UserData(rfid_xsps.ConnID, eAntennaNo._3.GetNum(), eReadType.Inventory, 0, 12);*/

		if (task == 1)
			rt = RFIDReader._Tag6C.GetEPC(rfid_xsps.ConnID, eAntennaNo._1.GetNum(), eReadType.Inventory);
		else
			rt = RFIDReader._Tag6C.GetEPC(rfid_xsps.ConnID, eAntennaNo._3.GetNum(), eReadType.Inventory);
		
		if (rt == 0)
			System.out.println("Thread " + Thread.currentThread().getId() + " # Task: " + task + " -> Success!");
		else
			System.out.println("Thread " + Thread.currentThread().getId() + " # Task: " + task + " -> Failure!" + rt);

		if (task == 1)
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		RFIDReader.Stop(rfid_xsps.ConnID);
	}

	int get_kika( ) {
		return 1;
	}
}

public class rfid_xsps {
/*	public static void main(String[] args) {
		System.out.println("Aloha");
	}*/
	static String ConnID = "192.168.1.116:9090";
	static long read_cnt = 0;

	public static void main(String[] args) throws Exception
	{
		System.out.println("Aloha");

//        Producer producer = new Producer();
        Publisher producer = new Publisher();
        RFIDConsumer consumer = new RFIDConsumer();
        RFIDTagListener rfidListener = new RFIDTagListener("RFID LISTENER");
 
        Thread producerThread = new Thread(producer);
        producerThread.start();
 
        Thread consumerThread = new Thread(consumer);
        consumer.registerListener(33, rfidListener);
        consumerThread.start();
//        Thread.sleep(1000);
//        consumer.registerListener(1, rfidListener);

//        Thread consumerThread2 = new Thread(consumer);
//        consumerThread2.start();		

        Thread.sleep(1000000);
		Scanner sc = new Scanner(System.in);

		String readKey;
		rfid_reader example = new rfid_reader();

		System.out.println("Please input TCP ConnID,Format: 'IP Address':'Connect Port' like \"192.168.1.116:9090\" \n");
//		ConnID = sc.next();
		if(RFIDReader.CreateTcpConn(ConnID, example))
		{
			System.out.println("Connect success!\n");
			try {
				System.out.println("Thread " + Thread.currentThread().getId() + " Config: " + RFIDReader._Config.GetEPCBaseBandParam(ConnID));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RFIDReader.Stop(ConnID);
//			break;
		}
		else
		{
			System.out.println("Connect failure!\n");
//			continue;
		}

/*		for (int i = 1; i < 2; i++) {
            rfid_task xsps_rfid_task
                = new rfid_task(i);
            xsps_rfid_task.start();
        }*/
        rfid_task xsps_rfid_task1 = new rfid_task(2);
//        rfid_task xsps_rfid_task2 = new rfid_task(2);

        xsps_rfid_task1.start();
/*        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        xsps_rfid_task2.start();*/

        try {
			System.out.println("Thread " + Thread.currentThread().getId() + " : Waiting for thread join!\n");
			xsps_rfid_task1.join();
			System.out.println("Thread " + Thread.currentThread().getId() + "Read counter: " + read_cnt + " : Task 1 joined!\n");
/*	        xsps_rfid_task2.join();
			System.out.println("Task 2 joined!\n");
			Thread.sleep(6000);
			System.out.println("Almost done!\n");*/
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		RFIDReader.Stop(ConnID);
        RFIDReader.CloseConn(ConnID);

/*
		System.out.println("Please select USB device:");
		try
		{
			int i = 1;
			HashMap<Integer, String> usb_devices = new HashMap<Integer, String>(); 
			for(String item : RFIDReader.GetUsbHidDeviceList())
			{
				System.out.println(String.valueOf(i) + "." + item);
				usb_devices.put(i, item);
				i++;
			}

			readKey = sc.next();
			i = Integer.valueOf(readKey);
			ConnID = usb_devices.get(i);
			if(RFIDReader.CreateUsbConn(ConnID, example))
			{
				System.out.println("Connect success!\n");
//				break;
			}
			else
			{
				System.out.println("Connect failure!\n");
//				continue;
			}
		}
		catch(Exception ex)
		{
			System.err.println("UsbConnectReader: " + ex.getMessage());
		}
*/
	}
}

/*
		int rt;

//		EPC = EB0CAF77884512D699000015
		String epc_org = new String("EB0CAF77884512D699000016");
		String epc_wms = new String("EB0CAF77884512D699000017");
/*		rt = RFIDReader._Tag6C.WriteEPC_MatchEPC(ConnID, eAntennaNo._1.GetNum(), epc_wms, epc_org, 0);
		if (rt == 0) {
			System.out.println("Write Success!");
		} else {
			System.out.println("Write Failure!" + rt);
		}*/

/*		rt = RFIDReader._Tag6C.Lock_MatchEPC(ConnID, eAntennaNo._1.GetNum(), eLockArea.userdata, eLockType.Unlock, epc_wms, 2, "00000000");
		if (rt == 0) {
			System.out.println("Lock Success!");
		} else {
			System.out.println("Lock Failure!" + rt);
		}

		String epc_data = new String("393132333435");
		rt = RFIDReader._Tag6C.WriteUserData_MatchEPC(ConnID, eAntennaNo._1.GetNum(), epc_data, 0, epc_wms, 0);
		if (rt == 0) {
			System.out.println("Write Success!");
		} else {
			System.out.println("Write Failure!" + rt);
		}*/

/*		RFIDReader._Config.SetReader (ConnID, eAntennaNo._2);
		if (rt == 0)
			System.out.println("SET OK");
		else
			System.out.println("SET FAILED");*/
		
//		rt = RFIDReader._Tag6C.GetEPC(ConnID, eAntennaNo._1.GetNum(), eReadType.Inventory);
//		EB0CAF77
//		String match_epc = new String("EB0CAF"); 
//		rt = RFIDReader._Tag6C.GetEPC_MatchEPC(ConnID, eAntennaNo._1.GetNum(), eReadType.Inventory, match_epc);		
/*		rt = RFIDReader._Tag6C.GetEPC_TID_UserData(ConnID, eAntennaNo._1.GetNum(), eReadType.Inventory, 0, 12);
		if (rt == 0) {
			System.out.println("Success!");
		} else {
			System.out.println("Failure!" + rt);
		}*/
/*				eMatchCode., matchCode: tid,
				matchWordStartIndex: 0, accessPassword: "00000000");*/
/*				matchType: eMatchCode.TID, matchCode: tid,
				matchWordStartIndex: 0, accessPassword: "00000000");*/


/*
public class rfid_xsps implements IAsynchronousMessage,ISearchDevice {
//	public static void main(String[] args) {
//		System.out.println("Aloha");
//	}
	static String ConnID = "";
	
	public static void main(String[] args)
    {
		
    }

	@Override
	public void DebugMsg(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeviceInfo(com.rfidread.Models.Device_Model arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GPIControlMsg(GPI_Model gpi_model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OutPutTags(Tag_Model arg0) {
		// TODO Auto-generated method stub
		System.out.println("EPC："+ arg0._EPC + " TID：" + arg0._TID + " Userdata:" + arg0._UserData + " ReaderName：" + arg0._ReaderName);
	}

	@Override
	public void OutPutTagsOver() {
		// TODO Auto-generated method stub
		System.out.println("OutPutTagsOver");
	}

	@Override
	public void PortClosing(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PortConnecting(String connID) {
		// TODO Auto-generated method stub
		System.out.println(connID);
        if (RFIDReader.GetServerStartUp())
        {
        	System.out.println("A reader connected to this server: " + connID);
            ConnID = connID;
        }
	}

	@Override
	public void WriteDebugMsg(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WriteLog(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
*/