package webMetrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;

public class MyMetricsListener extends MetricsServlet.ContextListener {

  public static final MetricRegistry METRIC_REGISTRY = new SharedRegistry().getRegistry();

  @Override
  protected MetricRegistry getMetricRegistry() {
    return METRIC_REGISTRY;
  }

}