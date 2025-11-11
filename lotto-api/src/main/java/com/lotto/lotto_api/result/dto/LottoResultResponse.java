package com.lotto.lotto_api.result.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LottoResultResponse {
    private Long id;
    private Long drawNo;
    private Long purchaseId;
    private List<Integer> purchasedNumbers;
    private Integer matchCount;
    private Boolean bonusMatch;
    private Integer rank;
    private Long prizeAmount;
}
