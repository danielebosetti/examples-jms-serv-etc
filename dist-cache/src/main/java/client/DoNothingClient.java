package client;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.MapListener;

@ComponentScan(basePackages = {"config"})
public class DoNothingClient {
  private static final Logger logger = LoggerFactory.getLogger(DoNothingClient.class);

  @Autowired
  private IMap<?, ?> map1;

  public static void main(String[] args) {

    String hz_config = System.getProperty("hazelcast.config");
    logger.info("hz-config={}", hz_config);
    URL res1 = System.class.getResource("/hazelcast.xml");
    logger.info("res1={}", res1);
    
    URL res2 = System.class.getResource("/hazelcast-default.xml");
    logger.info("res2={}", res2);
    
    try {
      String xml1 = IOUtils.toString(
          System.class.getResourceAsStream("/hazelcast-default.xml"), "UTF-8");
      logger.info("hazelcast-xml={}", xml1);
      
  } catch (IOException e) {
      e.printStackTrace();
  }
    
    
    
    @SuppressWarnings("resource")
    ApplicationContext context = new AnnotationConfigApplicationContext(DoNothingClient.class);

    DoNothingClient ndc = context.getBean(DoNothingClient.class);

    try {
      ndc.go();
    } catch (Exception e) {
      logger.info("err:", e);
      throw e;
    }
    
    util.TestUtil.sleepSafe(3600_000L);

  }

  private void go() {
    MapListener listener = new PersonListener();
    map1.addEntryListener(listener, true);
  }

}
