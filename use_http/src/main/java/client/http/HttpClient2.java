package client.http;

import java.net.*;

import io.netty.example.http.file.HttpStaticFileServer;

import java.io.*;

public class HttpClient2 {
  static String[] requestHeader= new String[] {
      "Host: localhost",
      "Accept: text/html,*/*",
      "Accept-Language: en-us",
      "Accept-Encoding: gzip, deflate",
      "User-Agent: Mozilla/5.0 (Windows NT 10.0;) Gecko/20100101 Firefox/63.0"
  };

  
  public static void main(String[] args) throws IOException {
//    Socket socket = new Socket("localhost", 80);
    Socket socket = new Socket("localhost", HttpStaticFileServer.SERVER_PORT );
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//    out.println("GET /a.html HTTP/1.0");
//    out.println("GET /favicon.ico HTTP/1.0");
    out.println("GET /target/classes/io/netty/example/echo/EchoClient.class HTTP/1.0");
    
      for (String item:requestHeader) {
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
