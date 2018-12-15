package client.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpClient {

  private static final String LINE_END = "\r\n";

  public static void main(String[] args) throws Exception {

    Socket clientSocket = new Socket(ClientConstants.HTTP_SERVER_HOST, ClientConstants.HTTP_PORT);

    DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inReader =
        new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    String request = ClientConstants.HTTP_GET_REQ_1;
    
    outStream.writeBytes(request);
    outStream.writeBytes(LINE_END);
    for (String item: ClientConstants.HTTP_REQ_HEADER) {
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
