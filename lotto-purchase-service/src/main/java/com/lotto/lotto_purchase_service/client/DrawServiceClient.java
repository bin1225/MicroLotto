package com.lotto.lotto_purchase_service.client;

import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class DrawServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.draw-service.url}")
    private String drawServiceUrl;

    public Long getCurrentDrawNo() {
        CurrentDrawResponse response = restTemplate.getForObject(
                drawServiceUrl + "/api/draws/current",
                CurrentDrawResponse.class
        );
        return response.getDrawNo();
    }

    @Retry(name = "drawService")
    @CircuitBreaker(name = "drawService", fallbackMethod = "getCurrentDrawNoFallbackValue")
    public Long getCurrentDrawNo(int delay, int faultPercent) {
        CurrentDrawResponse response = restTemplate.getForObject(
                drawServiceUrl + "/api/draws/current/{delay}/{faultPercent}", CurrentDrawResponse.class
                , delay, faultPercent
        );

        return response.getDrawNo();
    }

    public Long getCurrentDrawNoFallbackValue(int delay, int faultPercent, CallNotPermittedException ex) {

        return -1L;
    }
}
