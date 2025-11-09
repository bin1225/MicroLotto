package com.lotto.lotto_purchase_service.application.dto;

import com.lotto.lotto_purchase_service.domain.entity.Purchase;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseListResponse {
    private int totalCount;
    private List<PurchaseResponse> purchases;

    public static PurchaseListResponse from(List<Purchase> purchases) {
        return PurchaseListResponse.builder()
                .totalCount(purchases.size())
                .purchases(purchases.stream()
                        .map(PurchaseResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
