import java.util.Properties;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;


public class JMSSubscriber implements MessageListener {
	private static final String DEFAULT_CONNECTION_FACTORY = "TopicConnectionFactory";
	private static final String DEFAULT_DESTINATION = "topic/mySampleQueue";
	private static final String INITIAL_CONTEXT_FACTORY = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
	private static final String PROVIDER_URL = "tcp://localhost:6616";
	public JMSSubscriber() {
		try {
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY,INITIAL_CONTEXT_FACTORY);
			props.put(Context.PROVIDER_URL,System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));		
			javax.naming.Context ctx = new javax.naming.InitialContext(props);
			
			
			TopicConnectionFactory topicConnectionFactory =
	                (TopicConnectionFactory) ctx.lookup(DEFAULT_CONNECTION_FACTORY);
			
			  TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
			
			TopicSession topicSession =
	                topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = topicSession.createTopic(DEFAULT_DESTINATION);
			
			TopicSubscriber subscriber=topicSession.createSubscriber(topic);				
			topicConnection.start();				
			subscriber.setMessageListener((MessageListener) this);
			System.out.println("Esperando mensajes ...");
			
			//topicConnection.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	@Override
	public void onMessage(Message mensaje) {
		
		try {
			TextMessage msg=(TextMessage)mensaje;
			System.out.println(msg.getText());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		new Thread(){
			public void run(){
				new JMSSubscriber();
			}
		}.start();
		
	}
}
