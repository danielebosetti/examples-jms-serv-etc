package queue_ordering_check;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import factory.JMS_Factory;
import util.Util;

public class Queue_Order_Check {

  final static Logger LOG = LoggerFactory.getLogger(Queue_Order_Check.class);
  final static Logger LOG_send = LoggerFactory.getLogger("log_send");
  final static Logger LOG_rec = LoggerFactory.getLogger( "log_rec" );
  private static final String TAG_ID="id";
  private static final String TAG_TST="tst";


  public static void main(String[] args) throws Exception {

    Queue_Order_Check o = new Queue_Order_Check();
    o.go_test();

  }


  @Test
  public void go_test() throws Exception {
    JMS_Factory jf = new JMS_Factory();
    List<Session> sess_prod = new ArrayList<>(8);

    try (
        Connection conn = jf.getConn() ;
        Session s_prod = conn.createSession();
        ) {

      try {

        for (int i=0;i<1;i++) { 
          sess_prod.add(conn.createSession());
        }
        conn.start();

        String queueName = "exampleQueue";
        Destination destination = s_prod.createQueue(queueName );


        List<MessageConsumer> listCons = new ArrayList<>();
        
        // DON T create consumers that you dont use, otherwise they ll receive and buffer
        // messages without processing them!
        //for (Session item:sess_prod) {
          //listCons.add( item.createConsumer(destination) );
        //}
        listCons.add( sess_prod.get(0) .createConsumer(destination) );
        

        MessageProducer prod = s_prod.createProducer(destination);

        ExecutorService es_prod = Executors.newFixedThreadPool(2);
        ExecutorService es_cons = Executors.newFixedThreadPool(8);

        es_prod.submit( ()-> {
          int counter=0;
          while( counter<1_000) {
            try{
              MapMessage mm = s_prod.createMapMessage();
//              LOG_send.info("Sending id={}", counter);
              mm.setLongProperty(TAG_ID, counter++);
              mm.setLongProperty(TAG_TST, System.currentTimeMillis() );
              prod.send(mm);
            } catch (Exception e) { }
          }
        }) ;


        //      for ( MessageConsumer item:listCons) {
        MessageConsumer item = listCons.get(0);
        {
          es_cons.submit(  ()-> {
            try{
              while(true) {
                Message mm = item.receive();
                LOG_rec.info("id={} delay={}", 
                    mm.getLongProperty(TAG_ID),
                    System.currentTimeMillis()-mm.getLongProperty(TAG_TST)
                    
                    );
              }
            } catch (Exception e) { }
          }
              ) ;

        }

        LOG.info("Main waiting..");
        Util.sleepMillis(200_000);

        LOG.info("Shutting down..");

        for (MessageConsumer item1:listCons)
          item1.close();


        //clearing up ..
        es_cons.shutdownNow();
        es_prod.shutdownNow();

        LOG.info("Awaiting..");

        es_cons.awaitTermination(1_000,TimeUnit.MILLISECONDS);
        es_prod.awaitTermination(1_000,TimeUnit.MILLISECONDS);

        Util.sleepMillis(1_000);


        LOG.info("Main waiting..");
        Util.sleepMillis(1_000);

      }finally {
        for (Session item:sess_prod)
          item.close();

      }
    }
  }
}