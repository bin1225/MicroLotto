package com.lotto.lotto_api.purchase.dto;

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
}

