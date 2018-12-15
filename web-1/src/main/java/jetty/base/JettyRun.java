package jetty.base;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyRun implements Runnable {
  static private final Logger logger = LoggerFactory.getLogger("test.jettyStart");

  @Override
  public void run() {
    try {
      logger.info("starting");
      Server server = new Server(8299);
      setupServer(server);
      server.start();

      logger.info("started");
      //String dump = server.dump();
      //logger.info("started, dump=\n{}\n", dump);
      // server.join();
    } catch (Exception e) {
      logger.error("err:", e);
    }
  }

  private void setupServer(Server server) {
    WebAppContext webapp = new WebAppContext();
    File warFile = new File( WAR_ABSOLUTE_PATH);
    if (!warFile.exists()) {
      throw new RuntimeException("Unable to find WAR File: " + warFile.getAbsolutePath());
    }
    webapp.setWar(warFile.getAbsolutePath());
    webapp.setExtractWAR(true);
    server.setHandler(webapp);
  }
  
  private static final String WAR_ABSOLUTE_PATH =
      "C:\\Users\\danielebosetti.hp\\Desktop\\ws-misc\\proj\\webapps\\war-1\\target\\war-1-0.0.1-SNAPSHOT.war";
}
