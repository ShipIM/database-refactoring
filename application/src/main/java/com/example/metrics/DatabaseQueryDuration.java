package com.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class DatabaseQueryDuration {

    private final Timer dbQueryDurationTimer;

    public DatabaseQueryDuration(MeterRegistry meterRegistry) {
        this.dbQueryDurationTimer = Timer.builder("db_query_duration_seconds")
                .description("Database query duration")
                .register(meterRegistry);
    }

    public void record(Runnable query) {
        dbQueryDurationTimer.record(query);
    }

    public <T> T record(Supplier<T> query) {
        return dbQueryDurationTimer.record(query);
    }

}
