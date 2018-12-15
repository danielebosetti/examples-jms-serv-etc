package client.tcp;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import client.http.ClientConstants;
import util.PressKey;

public class ClientBuffStream {

  public static void main(String[] args) throws Exception {
    ClientBuffStream o = new ClientBuffStream();
    o.start();
  }

  private DataOutputStream outStream;
  private InputStream inputStream;
  private Socket clientSocket;

  private void start() throws Exception {
    registerShutdownHook();
    setMaxRunningTime(5000_000);
    PressKey.listenKey();

    clientSocket = new Socket(ClientConstants.HTTP_SERVER_HOST, ClientConstants.HTTP_PORT);

    outStream = new DataOutputStream(clientSocket.getOutputStream());
    inputStream = new BufferedInputStream(clientSocket.getInputStream(), 65536);

    for (int i = 0; i < 8; i++) {

      String request = "HELLO my name is bob num="+i;
      outStream.writeBytes(request);
      outStream.writeBytes("\n");
      outStream.flush();
    }

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
