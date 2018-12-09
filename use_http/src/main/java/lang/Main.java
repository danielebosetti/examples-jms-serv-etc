package lang;

import java.net.URL;

public class Main {
  public static void main(String[] args) {
    URL url = System.class.getResource("/java/lang/String.class");
    System.out.println("hello world!");
    System.out.println("using string class from url="+url);
  }
}
