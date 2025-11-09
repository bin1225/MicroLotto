package com.lotto.lotto_purchase_service.application.dto;

import com.lotto.lotto_purchase_service.domain.entity.Purchase;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseResponse {
    private Long id;
    private Long drawNo;
    private List<Integer> numbers;
    private LocalDateTime purchasedAt;

    public static PurchaseResponse from(Purchase purchase) {
        return PurchaseResponse.builder()
                .id(purchase.getId())
                .drawNo(purchase.getDrawNo())
                .numbers(purchase.getLottoNumbers().getNumberList())
                .purchasedAt(purchase.getPurchasedAt())
                .build();
    }
}
