package util;

public class TestUtil {

  public static void sleepSafe(long millis) {
    try {
      Thread.sleep(millis );
    } catch (InterruptedException e) {  }
  }

}
