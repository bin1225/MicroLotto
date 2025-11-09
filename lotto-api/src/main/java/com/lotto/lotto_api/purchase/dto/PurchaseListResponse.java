package com.lotto.lotto_api.purchase.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseListResponse {
    private int totalCount;
    private List<PurchaseResponse> purchases;
}
