package util;

public class Util {

  public static void sleepMillis(long millis) {
    try{Thread.sleep( millis );}
    catch (InterruptedException e) { }
  }
  
  
}
