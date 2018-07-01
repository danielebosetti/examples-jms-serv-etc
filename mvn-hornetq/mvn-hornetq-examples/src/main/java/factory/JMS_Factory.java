package factory;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;

public class JMS_Factory {

  public JMS_Factory() {
  }

  // returns a (closed) connection
  public Connection getConn() throws JMSException {

    Map<String, Object> cp = new HashMap<String, Object>();
    cp.put(TransportConstants.PORT_PROP_NAME, 5445);
    cp.put(TransportConstants.HOST_PROP_NAME, "localhost");
    cp.put(TransportConstants.HOST_PROP_NAME, "52.90.46.2");
    TransportConfiguration tc = 
        new TransportConfiguration(NettyConnectorFactory.class.getName(), cp);
    ConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF,tc);
    Connection connection = cf.createConnection();
    return connection ;
  }

}
