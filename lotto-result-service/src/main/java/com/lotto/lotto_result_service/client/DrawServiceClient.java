package com.lotto.lotto_result_service.client;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrawServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.draw-service.url}")
    private String drawServiceUrl;

    public WinningNumberResponse getWinningNumber(Long drawNo) {
        String url = drawServiceUrl + "/api/winning-numbers/" + drawNo;

        return restTemplate.getForObject(url, WinningNumberResponse.class);
    }
}
