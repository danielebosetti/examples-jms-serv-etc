package client;

import com.hazelcast.config.ManagementCenterConfig;

public class ManagemCenter {
  
  public static void main(String[] args) {
    
    
    ManagementCenterConfig manCenterCfg = new ManagementCenterConfig();
    manCenterCfg.setEnabled(true).setUrl("http://localhost:8080/mancenter");

      util.TestUtil.sleepSafe(1000000);
  }

}
