package webMetrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

public class WorkerThread implements Runnable {
  static private Logger log = LoggerFactory.getLogger("test.worker");
  private Histogram histo;

  SharedRegistry shared_reg = new SharedRegistry();
  private final MetricRegistry metrics = shared_reg.getRegistry();
  {
    histo = metrics.histogram( "histo.worker.times");
  }

  @Override
  public void run() {
    while(true) {
      try {
        long m = (long) (Math.random()*1000)+1000;
        Thread.sleep(m);
        histo.update(m);
//        log.info("m={}", m);
        
      } catch (InterruptedException e) {
        return;
      }
    }
  }
  
}
