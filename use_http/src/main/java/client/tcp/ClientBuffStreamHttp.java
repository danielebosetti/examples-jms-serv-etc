package client.tcp;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import client.http.ClientConstants;
import util.PressKey;

public class ClientBuffStreamHttp {

  public static void main(String[] args) throws Exception {
    ClientBuffStreamHttp o = new ClientBuffStreamHttp();
    o.start();
  }

  private DataOutputStream outStream;
  private InputStream inputStream;
  private Socket clientSocket;

  private void start() throws Exception {
    registerShutdownHook();
    setMaxRunningTime(5_000);
    PressKey.listenKey();

    clientSocket = new Socket(ClientConstants.HTTP_SERVER_HOST, ClientConstants.HTTP_PORT);

    outStream = new DataOutputStream(clientSocket.getOutputStream());
    inputStream = new BufferedInputStream(clientSocket.getInputStream(), 65536);

    outStream.write(ClientConstants.HTTP_GET_REQ_1.getBytes());
    outStream.write("\n".getBytes());

    for (String item : ClientConstants.HTTP_REQ_HEADER) {
      outStream.write(item.getBytes());
      outStream.write("\n".getBytes());
    }
    outStream.write("\n".getBytes());
    outStream.flush();
    
    
    while (true) {

      int av;
      do {
        av = inputStream.available();
      } while (av == 0);

      byte[] buff = new byte[av];
      inputStream.read(buff, 0, av);

      String line = new String(buff);
      System.out.println(line);

    }
  }

  private void setMaxRunningTime(final int millis) {
    new Thread() {
      public void run() {
        try {
          System.out.println("max running time millis="+millis);
          Thread.sleep(millis);
        } catch (Exception e) {
          e.printStackTrace(System.out);
        }
        System.exit(0);
      };
    }.start();
  }
  private void registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          System.out.println("running shutdownHook");
          if(inputStream!=null) inputStream.close();
          if(outStream!=null) outStream.close();
          if(clientSocket!=null) clientSocket.close();
          
          Thread.sleep(1000);
        } catch (Exception e) {
          e.printStackTrace(System.out);
        }
      }
    });
  }
}
