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
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jms.MessageListener;

public class rfid_xsps {
	static String ConnID = "192.168.1.116:9090";
	static long read_cnt = 0;

	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, Integer> antennaSetConsumers = new HashMap<Integer, Integer>(4);
		Vector<Integer> antennaSet = null;
		System.out.println("Aloha");

		antennaSetConsumers.put(1, 1);
		antennaSetConsumers.put(2, 1);
		antennaSetConsumers.put(3, 1);
		antennaSetConsumers.put(4, 1);

		antennaSet = new Vector(antennaSetConsumers.size());
		for (Map.Entry<Integer, Integer> set : antennaSetConsumers.entrySet())
			antennaSet.add(set.getKey());		

		XSPSRFIDReader producer = new XSPSRFIDReader("192.168.1.116", 9090, "wms_rfid.ant.", antennaSet);
		PoolRFIDConsumer consumers = new PoolRFIDConsumer("wms_rfid.ant.", "tcp://localhost:61616", antennaSetConsumers);

        Thread producerThread = new Thread(producer);
        producerThread.start();

        consumers.startRFIDConsumers();

        antennaSet.clear();
        antennaSet.add(1);
        antennaSet.add(3);

        Callable<HashMap<String, RFIDTag>> rfidInventory = new RFIDInventory("     RFID LISTENER ==========> ", consumers,
        		antennaSet, 50, 10000);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HashMap<String, RFIDTag>> future = executorService.submit(rfidInventory);

        HashMap<String, RFIDTag> result = future.get();
		System.out.println("Aloha Ending: " + result.size());
		Integer totalRead = 0;
		for (RFIDTag rfidTag : result.values()) {
			System.out.println("\t\t\tTag: " + rfidTag.get_EPC() + " @ Antenna: " + rfidTag.get_ANT_NUM() + " Count: " + rfidTag.getReadCount());
			totalRead += rfidTag.getReadCount();
		}
		System.out.println("Aloha Ending: Total read count -> " + totalRead);

//		for (RFIDTag rfidTag : result)
//			System.out.println("\t\t\tTag: " + rfidTag.get_EPC() + " @ Antenna: " + rfidTag.get_ANT_NUM());

        /* Just hang out right here to give a chance to scan some RFID Tags */
        Thread.sleep(5000);
        consumers.printConsumersStat();
	}
}