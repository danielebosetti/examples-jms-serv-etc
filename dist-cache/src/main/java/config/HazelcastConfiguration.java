package config;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Configuration
public class HazelcastConfiguration {
  private static final Logger logger = LoggerFactory.getLogger(
      HazelcastConfiguration.class);
      
//  @Bean
  public Config config() {
    Config res = new Config()
        .setInstanceName("test-1")
        
        .setGroupConfig(new GroupConfig("dev", "dev-pass"))
        .setManagementCenterConfig(
            new ManagementCenterConfig("http://localhost:8080/mancenter", 3)
            .setEnabled(true) )
        .setProperty("hazelcast.logging.type","slf4j");
    logger.info("config: bean config={}", res );
    return res;
  }

  @Bean
  public Config config__xml() {
    URL cfg_res = System.class.getResource("/hazelcast-1.xml");
    Config res;
    try {
      res = new XmlConfigBuilder(cfg_res).build()
          .setInstanceName("test-1")
          ;
      
    } catch (IOException e) {
      logger.error("err:", e);
      throw new RuntimeException(e);
    }
    logger.info("config: bean config={}", res );
    return res;
  }

  @Bean
  HazelcastInstance getInstance(Config config) {
    HazelcastInstance instance = Hazelcast.getOrCreateHazelcastInstance(config);
    logger.info("getInstance: bean res={}", instance);
    
    return instance;
  }
  
  @Bean(name="hz.data.map.1")
  public IMap<Object, Object> getMap1(HazelcastInstance hz) {
    return hz.getMap("map-1");    
  }

}