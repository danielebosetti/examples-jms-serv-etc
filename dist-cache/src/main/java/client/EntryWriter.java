package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import config.HazelcastConfiguration;
import domain.Person;
import domain.PersonFactory;
import util.TestUtil;

public class EntryWriter {
  private static final Logger logger = LoggerFactory.getLogger(
      EntryWriter.class);
  
  private static final long MAX_ID = 100_000L;

  public static void main(String[] args) {
    
    EntryWriter ew = new EntryWriter();
    try {
      ew.go();
    } catch (Exception e) {
      logger.info("err:", e);
      throw e;
    }
  }

  private void go() {
    AnnotationConfigApplicationContext context = 
        new AnnotationConfigApplicationContext( HazelcastConfiguration.class );
    
    HazelcastInstance hz = context.getBean(HazelcastInstance.class);
    
    IMap<Object, Object> map1 = hz.getMap("map-1");
    
    PersonFactory pf = new PersonFactory();

    long id=0;
    String name;
    
    while(true) {
      name="name-"+id;
      long id_=id%MAX_ID;
      Person item = pf.of(id, name);
      long start = System.currentTimeMillis();
      
      map1.put(id_, item);
      long end=System.currentTimeMillis();
    //  logger.info("added, time={}", end-start);
      
      
      long millis = 5;
      TestUtil.sleepSafe(millis);
      id++;
    }
    
    
  }
  
}