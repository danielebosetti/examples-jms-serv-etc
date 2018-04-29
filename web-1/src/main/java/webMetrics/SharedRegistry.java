package webMetrics;

import com.codahale.metrics.MetricRegistry;

public class SharedRegistry {
  
  static MetricRegistry metricRegistry = new MetricRegistry();

  public MetricRegistry getRegistry() {
    return metricRegistry;
  }

}
