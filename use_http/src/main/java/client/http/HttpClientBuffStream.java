package client.http;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class HttpClientBuffStream {

  public static void main(String[] args) throws Exception {

    Socket clientSocket = new Socket("google.com", 80);

    DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
    InputStream inputStream = new BufferedInputStream( clientSocket.getInputStream(), 65536);

    String request = "GET / HTTP/1.1";
    outStream.writeBytes(request);
    outStream.writeBytes("\r\n");
    outStream.writeBytes("\r\n");

    int emptyLineCount = 0;
    char prev = 0;
    int lineSize = 0;

    while (true) {

      int av;
      do {
        av = inputStream.available();
      }
      while (av==0);
      
      System.out.println("available=" + av);
      byte[] buff = new byte[av];
      inputStream.read(buff, 0, av);

      for (byte b : buff) {
        char curr = (char) b;
        System.out.printf("%c", curr);
        if (curr == '\n' && prev == '\r') {
          if (lineSize == 0) {
            emptyLineCount++;
          }
          lineSize = 0;
        } else {
          if (curr != '\n' && curr != '\r') {
            lineSize++;
          }
        }
        prev = curr;
      }

      if (emptyLineCount == 2)
        break;
    }

    inputStream.close();
    outStream.close();
    clientSocket.close();
  }
}
