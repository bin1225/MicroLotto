package com.lotto.lotto_draw_service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResultServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.result-service.url}")
    private String resultServiceUrl;

    public void requestResultCalculation(Long closedDrawNo) {
        String url = "/api/result/calculate/" + closedDrawNo;
        restTemplate.postForEntity(resultServiceUrl + url, null, String.class);
    }
}
