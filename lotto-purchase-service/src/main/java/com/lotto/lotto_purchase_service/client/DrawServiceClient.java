package com.lotto.lotto_purchase_service.client;

import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrawServiceClient {

    private static final String CURRENT_DRAW_ENDPOINT = "/api/draws/current";
    private static final String CURRENT_DRAW_WITH_FAULT_ENDPOINT = "/api/draws/current/{delay}/{faultPercent}";
    private static final Long FALLBACK_DRAW_NO = -1L;

    private final RestTemplate restTemplate;

    @Value("${services.draw-service.url}")
    private String drawServiceUrl;

    public Long getCurrentDrawNo() {
        CurrentDrawResponse response = fetchCurrentDraw();
        return response.getDrawNo();
    }

    @Retry(name = "drawService")
    @CircuitBreaker(name = "drawService", fallbackMethod = "handleDrawServiceFailure")
    public Long getCurrentDrawNoWithFaultTolerance(int delay, int faultPercent) {
        CurrentDrawResponse response = fetchCurrentDrawWithFault(delay, faultPercent);
        return response.getDrawNo();
    }

    private CurrentDrawResponse fetchCurrentDraw() {
        String url = buildUrl(CURRENT_DRAW_ENDPOINT);
        return restTemplate.getForObject(url, CurrentDrawResponse.class);
    }

    private CurrentDrawResponse fetchCurrentDrawWithFault(int delay, int faultPercent) {
        String url = buildUrl(CURRENT_DRAW_WITH_FAULT_ENDPOINT);
        return restTemplate.getForObject(url, CurrentDrawResponse.class, delay, faultPercent);
    }

    private String buildUrl(String endpoint) {
        return drawServiceUrl + endpoint;
    }
}
