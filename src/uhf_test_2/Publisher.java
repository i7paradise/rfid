package uhf_test_2;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher implements Runnable{

    private static String brokerURL = "tcp://localhost:61616";
    private static transient ConnectionFactory factory;
    private transient Connection connection;
    private transient Session session;
    private transient MessageProducer producer;
    
    private static int count = 10;
    private static int total;
    private static int id = 1;
    
//    private String jobs[] = new String[]{"suspend", "delete"};
    private String jobs[] = new String[]{"wms_rfid"};
    
    public Publisher() throws JMSException {
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(null);

//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        //Create connection.
//        Connection connection = factory.createConnection();

        // Start the connection
//        connection.start();

        // Create a session which is non transactional
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create Destination queue
//        Destination queue = session.createQueue("Queue");

        // Create a producer
//        MessageProducer producer = session.createProducer(queue);
    }    
    
    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }    
    
//	public static void main(String[] args) throws JMSException {
    @Override
    public void run() {
    	try {
    		Publisher publisher = new Publisher();
    		while (total < 10) {
    			for (int i = 0; i < count; i++) {
    				publisher.sendMessage();
    			}
    			total += count;
    			System.out.println("Published '" + count + "' of '" + total + "' job messages");
    			try {
    				Thread.sleep(1000);
    			} catch (InterruptedException x) {
    	    		System.out.println("Consumer @ exeception: " + x.getMessage());
    			}
    		}
    		publisher.close();
    	} catch (Exception e) {
    	}
    }
	
    public void sendMessage() throws JMSException {
        int idx = 0;
/*        while (true) {
            idx = (int)Math.round(jobs.length * Math.random());
            if (idx < jobs.length) {
                break;
            }
        }*/
        String job = jobs[0];
        Destination destination = session.createQueue("JOBS." + job);
        RFIDTag wmsTag = new RFIDTag();
        wmsTag._ANT_NUM = 33;
        wmsTag._EPC = String.valueOf(id++);
        Message message = session.createObjectMessage(wmsTag);
        System.out.println("Sending: id: " + ((ObjectMessage)message).getObject() + " on queue: " + destination);
        producer.send(destination, message);
    }	

}
