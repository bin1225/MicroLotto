package com.lotto.lotto_result_service.client;

import com.lotto.lotto_api.purchase.dto.PurchaseListResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PurchaseServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.purchase-service.url}")
    private String purchaseServiceUrl;

    public List<PurchaseResponse> getPurchasesByDrawNo(Long drawNo) {
        String url = purchaseServiceUrl + "/api/purchases/draw/" + drawNo;

        PurchaseListResponse response = restTemplate.getForObject(url, PurchaseListResponse.class);

        if (response == null || response.getPurchases() == null) {
            return List.of();
        }

        return response.getPurchases();
    }
}
