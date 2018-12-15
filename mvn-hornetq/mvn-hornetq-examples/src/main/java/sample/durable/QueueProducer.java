package sample.durable;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueProducer {
  static Logger LOG = LoggerFactory.getLogger(QueueProducer.class);


  public static void main(String[] args) {

    ExecutorService es = Executors.newFixedThreadPool(3);

    for (int i=0;i<3;i++) {

      es.submit( new Runnable () {
        public void run() {

          QueueProducer o = new QueueProducer();
          try {
            o.runExample() ;
          } catch (Exception e) {
            LOG.error("Error >> ", e);
          }
        }
      }
          );
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
      Queue queue = (Queue)initialContext.lookup("/queue/exampleQueue");

      // Step 3. Look-up the JMS connection factory
      ConnectionFactory cf = (ConnectionFactory)initialContext.lookup("/ThroughputConnectionFactory");

      // Step 4. Create a JMS connection
      connection = cf.createConnection();
      connection.setClientID("asd");

      ConnectionMetaData metaData = connection.getMetaData();
      
      LOG.info("Client id={}", connection.getClientID() );
      LOG.info(" {}-{} {} {} {}", 
          metaData.getJMSMajorVersion(),  
          metaData.getJMSMinorVersion(),
          metaData.getJMSProviderName(),
          metaData.getJMSVersion(),
          metaData.getProviderVersion()
          );
      
      // Step 6. Start the connection
      connection.start();

      // Step 7. Create a JMS session
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Step 8. Create a JMS message producer
      MessageProducer messageProducer = session.createProducer(queue);

      // Step 10. Create a text message
      TextMessage message1 = session.createTextMessage("This is a text message 1");

      // Step 11. Send the text message to the topic
      long start = System.currentTimeMillis() ;
      int NUM_ = 10;
      for (int i=0; i<NUM_; i++) {
        messageProducer.send(message1);
 //       Util.sleepMillis(10_0);

      }
      long end = System.currentTimeMillis() ;
      LOG.info("Elapsed (msec)={} msgs={} rate (msgs/millisec)={}", end-start , NUM_, 1.*NUM_/(end-start));
      LOG.info("ClientID={}", connection.getClientID() );

      return true;
    }
    finally {
      if (connection != null) {
        connection.close();
      }
      if (initialContext != null) {
        initialContext.close();
      }
    }
  }
}




