package client.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import netty.echo.EchoServer;
import util.PressKey;

public class ClientBuffStream2 {

  public static void main(String[] args) throws Exception {
    ClientBuffStream2 o = new ClientBuffStream2();
    o.start();
  }

  private DataOutputStream outStream;
  private BufferedReader streamReader;
  private Socket clientSocket;

  private void start() throws Exception {
    registerShutdownHook();
    setMaxRunningTime(5000_000);
    PressKey.listenKey();

    clientSocket = new Socket("localhost", EchoServer.SERVER_PORT);

    outStream = new DataOutputStream(clientSocket.getOutputStream());
    streamReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    for (int i = 0; i < 8; i++) {

      String request = "HELLO my name is bob num=" + i;
      outStream.writeBytes(request);
      outStream.writeBytes("\n");
      outStream.flush();
    }

    String line;
    while ((line = streamReader.readLine()) != null) {
      System.out.println(line);
    }
  }

  private void setMaxRunningTime(final int millis) {
    new Thread() {
      public void run() {
        try {
          System.out.println("max running time millis=" + millis);
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
          if (streamReader != null)
            streamReader.close();
          if (outStream != null)
            outStream.close();
          if (clientSocket != null)
            clientSocket.close();

          Thread.sleep(1000);
        } catch (Exception e) {
          e.printStackTrace(System.out);
        }
      }
    });
  }
}
