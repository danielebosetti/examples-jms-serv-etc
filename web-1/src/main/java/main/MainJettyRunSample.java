package main;

import jetty.base.JettyRun;

public class MainJettyRunSample {
  public static void main(String[] args) {
    new Thread(new JettyRun(), "jetty-worker-start").start();
  }
}
