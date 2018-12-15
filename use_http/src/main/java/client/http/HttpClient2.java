package client.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpClient2 {

  public static void main(String[] args) throws IOException {
    Socket socket = new Socket(ClientConstants.HTTP_SERVER_HOST, ClientConstants.HTTP_PORT);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    out.println(ClientConstants.HTTP_GET_REQ_1);

    for (String item : ClientConstants.HTTP_REQ_HEADER) {
      out.println(item);
    }
    out.println();
    out.flush();

    String line;
    while ((line = in.readLine()) != null) {
      System.out.println(line);
    }
    in.close();
    out.close();
    socket.close();
  }
}
