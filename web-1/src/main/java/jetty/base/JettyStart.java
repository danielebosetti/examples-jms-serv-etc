package jetty.base;

import java.io.File;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyStart {
  static private Logger log = LoggerFactory.getLogger("test.jettyStart");

  public void startJetty() {
    new Thread(new WorkerRun(), "jetty-worker-start").start();
  }

  private class WorkerRun implements Runnable {
    @Override
    public void run() {
      try {
        log.info("starting");
        Server server = new Server(8080);

        // setupDirectoryListing(server);
        // setupRawServlet(server);
        setupWebapp(server);
        
        server.start();
        
        log.info("started");
        String dump = server.dump();
        log.info("started, dump=\n{}\n", dump);
        // server.join();
      } catch (Exception e) {
        log.error("err:", e);
      }
    }

    private void setupWebapp(Server server) {
      // The WebAppContext is the entity that controls the environment in
      // which a web application lives and
      // breathes. In this example the context path is being set to "/" so it
      // is suitable for serving root context
      // requests and then we see it setting the location of the war.
      WebAppContext webapp = new WebAppContext();
      // C:\Users\danielebosetti.hp\Desktop\proj\webapps\war-1\target\war-1-0.0.1-SNAPSHOT.war
      //File warFile = new File( "/Users/danielebosetti.hp/Desktop/proj/webapps/war-1/target/war-1-0.0.1-SNAPSHOT.war");
      //webapps\war-2\target\war-metrics-0.0.1-SNAPSHOT.war
      File warFile = new File( "../webapps/war-2/target/war-metrics-0.0.1-SNAPSHOT.war");

      if (!warFile.exists()) {
        throw new RuntimeException("Unable to find WAR File: " + warFile.getAbsolutePath());
      }
      webapp.setWar(warFile.getAbsolutePath());
      webapp.setExtractWAR(true);
      server.setHandler(webapp);
    }

    @SuppressWarnings("unused")
    private void setupRawServlet(Server server) {
      // The ServletHandler is a dead simple way to create a context handler
      // that is backed by an instance of a Servlet.
      // This handler then needs to be registered with the Server object.
      ServletHandler handler = new ServletHandler();
      server.setHandler(handler);
      // Passing in the class for the Servlet allows jetty to instantiate an
      // instance of that Servlet and mount it on a given context path.

      // IMPORTANT:
      // This is a raw Servlet, not a Servlet that has been configured
      // through a web.xml @WebServlet annotation, or anything similar.
      handler.addServletWithMapping(HelloServlet.class, "/*");
    }

    @SuppressWarnings("unused")
    private void setupDirectoryListing(Server server) {
      ResourceHandler resource_handler = new ResourceHandler();
      resource_handler.setDirectoriesListed(true);
      resource_handler.setResourceBase(".");
      HandlerList handlers = new HandlerList();
      handlers.setHandlers(new Handler[] {resource_handler, new DefaultHandler()});
      server.setHandler(handlers);
    }

  }
}


