package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jetty.base.JettyStart;
import webMetrics.WorkerThread;

public class MainSample {

  static private Logger log = LoggerFactory.getLogger("test.web");

  public static void main(String[] args) {
    log.info("main");

    new JettyStart().startJetty();
    new Thread(new WorkerThread()).start();
    
  }

}