import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;

public class JMSPublisher {

	private static final String DEFAULT_CONNECTION_FACTORY = "TopicConnectionFactory";
	private static final String DEFAULT_DESTINATION = "topic/mySampleQueue";
	private static final String INITIAL_CONTEXT_FACTORY = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
	private static final String PROVIDER_URL = "tcp://localhost:6616";
	public static void main(String[] args) throws JMSException, NamingException{
		// Step 1. Create an initial context to perform the JNDI lookup.
		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,INITIAL_CONTEXT_FACTORY);
		props.put(Context.PROVIDER_URL,System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));		
		javax.naming.Context ctx = new javax.naming.InitialContext(props);		
		// Step 2. perform a lookup on the topic
		TopicConnectionFactory topicConnectionFactory =
                (TopicConnectionFactory) ctx.lookup(DEFAULT_CONNECTION_FACTORY);
		// Step 3. perform a lookup on the Connection Factory
		  TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();
		
		// Step 5. Create a JMS Session
		TopicSession topicSession =
                topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = topicSession.createTopic(DEFAULT_DESTINATION);
		// Step 6. Create a JMS Message Producer
		MessageProducer sender=topicSession.createProducer(topic);
		topicConnection.start();
		TextMessage msg=topicSession.createTextMessage("Mensaje enviado: BUY AAPL 1000 SHARES JNDI");	
		sender.send(msg);
		System.out.println("Mensaje enviado");		
		topicConnection.close();			
	}
}