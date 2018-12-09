package client.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpClient {

  private static final String LINE_END = "\r\n";

  public static void main(String[] args) throws Exception {

//    Socket clientSocket = new Socket("google.com", 80);
    Socket clientSocket = new Socket("localhost", 80);

    DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inReader =
        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    String[] requestHeader= new String[] {
        "Host: localhost",
        "Accept: text/html,*/*",
        "Accept-Language: en-us",
        "Accept-Encoding: gzip, deflate",
        "User-Agent: Mozilla/5.0 (Windows NT 10.0;) Gecko/20100101 Firefox/63.0"
    };
    
//    String request = "GET /a.html HTTP/1.1";
    String request = "GET /favicon.ico HTTP/1.1";
    
    outStream.writeBytes(request);
    outStream.writeBytes(LINE_END);
    for (String item:requestHeader) {
      outStream.writeBytes(item);
      outStream.writeBytes(LINE_END);
    }
    outStream.writeBytes(LINE_END);

    int emptyLineCount = 0;

    while (true) {
      String line = inReader.readLine();
      if (line.length() == 0) {
        emptyLineCount++;
      }
      System.out.println(line);

      if (emptyLineCount == 2) {
        System.out.println("\n*** detected end of body (?) ***\n");
        break;
      }
    }

    inReader.close();
    outStream.close();
    clientSocket.close();
  }
}
