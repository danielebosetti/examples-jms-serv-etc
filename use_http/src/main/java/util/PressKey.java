package util;

import java.util.Scanner;

public class PressKey {

  public static void listenKey() {
    new PressKey().listenKey_();
  }

  public void listenKey_() {
    System.out.println("PressKey.listenKey_: press a key to exit");
    new Thread() {
      @Override
      public void run() {
        Scanner scan = new Scanner(System.in);
        while (true) {
          String input = scan.nextLine();
          if (input != null) {
            System.out.println("Exit program");
            scan.close();
            System.exit(0);
          }
        }
      }
    }.start();
  }
}
