package uhf_test_2;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class RFIDTagListener {

	private String job;
	
	public RFIDTagListener(String job) {
		this.job = job;
	}

/*	public void onMessage(Message message) {
		try {
			System.out.println(job + " id:" + ((ObjectMessage)message).getObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public void onMessage(RFIDTag rfidTag) {
		try {
			//do something here
			System.out.println(job + " Tag: " + rfidTag.get_ANT_NUM() + " # " + rfidTag.get_EPC());
//			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		
	}
	*/
}
