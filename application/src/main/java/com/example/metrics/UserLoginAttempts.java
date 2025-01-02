package com.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UserLoginAttempts {

    private final Counter loginAttemptsTotal;

    public UserLoginAttempts(MeterRegistry meterRegistry) {
        this.loginAttemptsTotal = Counter.builder("user_login_attempts_total")
                .description("Total user login attempts")
                .register(meterRegistry);
    }

    public void increment() {
        loginAttemptsTotal.increment();
    }

}
