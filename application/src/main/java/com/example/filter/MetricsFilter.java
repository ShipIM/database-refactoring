package com.example.filter;

import com.example.metrics.HttpRequestDuration;
import com.example.metrics.HttpRequestsErrors;
import com.example.metrics.HttpRequestsTotal;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MetricsFilter implements Filter {

    private final HttpRequestsTotal httpRequestsTotal;
    private final HttpRequestsErrors httpRequestsErrors;
    private final HttpRequestDuration httpRequestDuration;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse httpResponse) {
            httpRequestsTotal.increment();

            httpRequestDuration.record(() -> {
                try {
                    chain.doFilter(request, response);
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e);
                }
            });

            if (httpResponse.getStatus() >= 400) {
                httpRequestsErrors.increment();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

}
