package com.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestsErrors {

    private final Counter httpRequestsErrorsTotal;

    public HttpRequestsErrors(MeterRegistry meterRegistry) {
        this.httpRequestsErrorsTotal = Counter.builder("http_requests_errors_total")
                .description("Total HTTP request errors")
                .register(meterRegistry);
    }

    public void increment() {
        httpRequestsErrorsTotal.increment();
    }

}