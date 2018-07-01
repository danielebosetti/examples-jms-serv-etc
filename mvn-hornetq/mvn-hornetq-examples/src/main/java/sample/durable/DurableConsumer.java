package sample.durable;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DurableConsumer {
  static Logger LOG = LoggerFactory.getLogger(DurableConsumer.class);


  public static void main(String[] args) {
    DurableConsumer o = new DurableConsumer();
    try {
      o.runExample() ;
    } catch (Exception e) {
      LOG.error("Error >> ", e);
    }
  }

  protected InitialContext getContext(String serverId) throws Exception
  {
    Properties props = new Properties();
    props.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
    props.put("java.naming.provider.url", serverId );
    props.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
    return new InitialContext(props);
  }


  public boolean runExample() throws Exception
  {
    Connection connection = null;
    InitialContext initialContext = null;
    try
    {
      // Step 1. Create an initial context to perform the JNDI lookup.
      initialContext = getContext( "jnp://localhost:1099" );

      // Step 2. Look-up the JMS topic
      Topic topic = (Topic)initialContext.lookup("/topic/exampleTopic");

      // Step 3. Look-up the JMS connection factory
      ConnectionFactory cf = (ConnectionFactory)initialContext.lookup("/ThroughputConnectionFactory");

      // Step 4. Create a JMS connection
      connection = cf.createConnection();

      // Step 5. Set the client-id on the connection
      connection.setClientID("durable-client-2");

      // Step 6. Start the connection
      connection.start();

      // Step 7. Create a JMS session
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Step 8. Create a JMS message producer
      MessageProducer messageProducer = session.createProducer(topic);

      // Step 9. Create the subscription and the subscriber.
      TopicSubscriber subscriber = session.createDurableSubscriber(topic, "subscriber-1");
      LOG.trace( subscriber.toString() );

      // Step 10. Create a text message
      TextMessage message1 = session.createTextMessage("");

      // Step 11. Send the text message to the topic
      long start = System.currentTimeMillis() ;
      int NUM_ = 100_000;
      for (int i=0; i<NUM_; i++) {
        messageProducer.send(message1);
      }
      long end = System.currentTimeMillis() ;
      LOG.info("Elapsed={} msgs={} rate (msgs/millisec)={}", end-start , NUM_, NUM_/(end-start));


//      // Step 12. Consume the message from the durable subscription
//
//      TextMessage messageReceived = (TextMessage)subscriber.receive();
//
//      System.out.println("Received message: " + messageReceived.getText());
//
//      // Step 13. Create and send another message
//
//      TextMessage message2 = session.createTextMessage("This is a text message 2");
//
//      messageProducer.send(message2);
//
//      System.out.println("Sent message: " + message2.getText());
//
//      // Step 14. Close the subscriber - the server could even be stopped at this point!
//      subscriber.close();
//
//      // Step 15. Create a new subscriber on the *same* durable subscription.
//
//      subscriber = session.createDurableSubscriber(topic, "subscriber-1");
//
//      // Step 16. Consume the message
//
//      messageReceived = (TextMessage)subscriber.receive();
//
//      System.out.println("Received message: " + messageReceived.getText());
//
//      // Step 17. Close the subscriber
//      subscriber.close();
//
//      // Step 18. Delete the durable subscription
//      session.unsubscribe("subscriber-1");

      return true;
    }
    finally
    {
      if (connection != null)
      {
        // Step 19. Be sure to close our JMS resources!
        connection.close();
      }
      if (initialContext != null)
      {
        // Step 20. Also close the initialContext!
        initialContext.close();
      }
    }
  }
}




