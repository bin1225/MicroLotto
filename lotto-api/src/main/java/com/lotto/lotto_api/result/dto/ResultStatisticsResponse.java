package com.lotto.lotto_api.result.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultStatisticsResponse {
    private Long drawNo;
    private Long totalPurchases;
    private Map<Integer, Long> rankCounts;
    private Map<Integer, Long> rankPrizes;
    private Long totalPrizeAmount;
}
