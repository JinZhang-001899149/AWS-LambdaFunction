package com.cloud.assignment.assignment.Metrics;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsClientBean {

    @Value("${publishMetrics}")
    private boolean publiMetrics;

    private String metricsServerHost = "localhost";

    private int etricsServerPort = 8125;

    @Bean
    public StatsDClient metricsClient(){

        if (publiMetrics) {
            return new NonBlockingStatsDClient("csye6225",metricsServerHost, etricsServerPort);
        }
        return new NoOpStatsDClient();
    }
}
