package com.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class DatabaseQueriesTotal {

    private final Counter dbQueriesTotal;

    public DatabaseQueriesTotal(MeterRegistry meterRegistry) {
        this.dbQueriesTotal = Counter.builder("db_queries_total")
                .description("Total database queries")
                .register(meterRegistry);
    }

    public void increment() {
        dbQueriesTotal.increment();
    }

}
