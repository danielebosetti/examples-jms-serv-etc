package webMetrics;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;

public class MyHealthListener extends HealthCheckServlet.ContextListener {

  public static final HealthCheckRegistry HEALTH_REGISTRY = new HealthCheckRegistry();

  @Override
  protected HealthCheckRegistry getHealthCheckRegistry() {
    return HEALTH_REGISTRY;
  }

}