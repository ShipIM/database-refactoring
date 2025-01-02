package com.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestsTotal {

    private final Counter httpRequestsTotal;

    public HttpRequestsTotal(MeterRegistry meterRegistry) {
        this.httpRequestsTotal = Counter.builder("http_requests_total")
                .description("Total HTTP requests")
                .register(meterRegistry);
    }

    public void increment() {
        httpRequestsTotal.increment();
    }

}
