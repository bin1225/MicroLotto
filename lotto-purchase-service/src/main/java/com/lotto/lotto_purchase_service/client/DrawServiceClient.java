package com.lotto.lotto_purchase_service.client;

import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
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
}
