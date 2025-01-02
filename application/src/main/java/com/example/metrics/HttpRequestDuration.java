package com.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class HttpRequestDuration {

    private final Timer httpRequestDuration;

    public HttpRequestDuration(MeterRegistry meterRegistry) {
        this.httpRequestDuration = Timer.builder("http_request_duration_seconds")
                .description("Duration of HTTP requests")
                .register(meterRegistry);
    }

    public void record(Runnable runnable) {
        httpRequestDuration.record(runnable);
    }

    public <T> T record(Supplier<T> query) {
        return httpRequestDuration.record(query);
    }

}
