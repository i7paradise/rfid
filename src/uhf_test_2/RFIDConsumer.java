package uhf_test_2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class RFIDConsumer implements Runnable {

    private static String brokerURL = "tcp://localhost:61616";
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private MessageConsumer consumer;
    private Destination RFIDqueue;
    private ConcurrentHashMap<Integer, RFIDTagListener> rfidListener = new ConcurrentHashMap<Integer, RFIDTagListener>();
    
//    private String jobs[] = new String[]{"suspend", "delete"};
    private String jobs[] = new String[]{"JOBS.wms_rfid"};
    
    public RFIDConsumer() throws JMSException {
    	factory = new ActiveMQConnectionFactory(brokerURL);
//    	factory.setTrustAllPackages(true);
    	connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        RFIDqueue = session.createQueue("JOBS.wms_rfid");
        consumer = session.createConsumer(RFIDqueue);

//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        //Create Connection
//        Connection connection = factory.createConnection();

        // Start the connection
//        connection.start();

        // Create Session
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //Create queue
//        Destination queue = session.createQueue("Queue");

//        MessageConsumer consumer = session.createConsumer(queue);

    }
    
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }    
    
//    public static void main(String[] args) throws JMSException {
    public void run() {
    	try {
//    		RFIDConsumer wmsConsumer = new RFIDConsumer();
/*    		for (String job : consumer.jobs) {
    			Destination destination = consumer.getSession().createQueue("JOBS." + job);
    			MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
    			messageConsumer.setMessageListener(new Listener(job));
    		}*/
    		int cnt = 0;
            for (; ; ) {
            	Message message = consumer.receive();

            	cnt++;
            	if (message instanceof ObjectMessage) {
            		ObjectMessage objMessage = (ObjectMessage) message;
//                    SomeObject someObject = (SomeObject)objectMessage.getObject();
            		RFIDTag wmsTag = (RFIDTag) objMessage.getObject();
                    
            		for (Map.Entry<Integer, RFIDTagListener> set :
            			rfidListener.entrySet()) {
            
                       // Printing all elements of a Map
                       System.out.println(set.getKey() + " = "
                                          + set.getValue());
                       if (set.getKey() == wmsTag._ANT_NUM)
                    	   set.getValue().onMessage(wmsTag);
                   }
            		Thread.sleep(300);

//            		String text = textMessage.getText();
//            		System.out.println("Consumer Received cnt -> " + cnt + " out of " + consumer.toString() + " = " + wmsTag._EPC);
//            		System.out.println("Consumer Received cnt -> " + cnt + " out of " + consumer.toString() + " = " + objMessage.toString());
            	}
//            	Thread.sleep(5000);
            }
    	} catch(Exception e) {
    		System.out.println("Consumer @ exeception: " + e.getMessage());
    	}
    }

    public void registerListener(Integer antenna, RFIDTagListener _rfidListener) {
    	rfidListener.put(antenna, _rfidListener);
    }

    public void unregisterListener(Integer antenna, RFIDTagListener _rfidListener) {
    	if (rfidListener.containsKey(_rfidListener))
    		rfidListener.remove(antenna, _rfidListener);
    }

	public Session getSession() {
		return session;
	}


}
