package com.lotto.lotto_result_service.application.service;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import com.lotto.lotto_api.result.dto.LottoResultResponse;
import com.lotto.lotto_api.result.dto.ResultStatisticsResponse;
import com.lotto.lotto_result_service.client.DrawServiceClient;
import com.lotto.lotto_result_service.client.PurchaseServiceClient;
import com.lotto.lotto_result_service.domain.entity.LottoResultEntity;
import com.lotto.lotto_result_service.domain.repository.LottoResultEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultCalculationService {

    private final DrawServiceClient drawServiceClient;
    private final PurchaseServiceClient purchaseServiceClient;
    private final LottoResultEntityRepository resultRepository;

    private static final Map<Integer, Long> PRIZE_MAP = Map.of(
            1, 2_000_000_000L,  // 1등: 6개 일치
            2, 30_000_000L,     // 2등: 5개 + 보너스
            3, 1_500_000L,      // 3등: 5개 일치
            4, 50_000L,         // 4등: 4개 일치
            5, 5_000L           // 5등: 3개 일치
    );

    @Transactional
    public void calculateResults(Long drawNo) {

        // 1. draw-service에서 당첨 번호 조회
        WinningNumberResponse winningNumber = drawServiceClient.getWinningNumber(drawNo);

        // 2. purchase-service에서 해당 회차의 모든 구매 내역 조회
        List<PurchaseResponse> purchases = purchaseServiceClient.getPurchasesByDrawNo(drawNo);

        // 3. 각 구매 내역에 대해 결과 계산 및 저장
        List<LottoResultEntity> results = new ArrayList<>();
        for (PurchaseResponse purchase : purchases) {
            LottoResultEntity result = calculateSingleResult(
                    drawNo,
                    purchase,
                    winningNumber.getWinningNumbers(),
                    winningNumber.getBonusNumber()
            );
            results.add(result);
        }

        resultRepository.saveAll(results);
    }

    private LottoResultEntity calculateSingleResult(
            Long drawNo,
            PurchaseResponse purchase,
            List<Integer> winningNumbers,
            Integer bonusNumber
    ) {
        List<Integer> purchasedNumbers = purchase.getNumbers();

        // 당첨 번호와 일치하는 개수 계산
        long matchCount = purchasedNumbers.stream()
                .filter(winningNumbers::contains)
                .count();

        // 보너스 번호 일치 여부
        boolean bonusMatch = purchasedNumbers.contains(bonusNumber);

        // 등수 결정
        int rankValue = determineRank((int) matchCount, bonusMatch);

        // 상금 결정
        long prizeAmount = PRIZE_MAP.getOrDefault(rankValue, 0L);

        LottoResultEntity result = LottoResultEntity.builder()
                .drawNo(drawNo)
                .purchaseId(purchase.getId())
                .matchCount((int) matchCount)
                .bonusMatch(bonusMatch)
                .rankValue(rankValue)
                .prizeAmount(prizeAmount)
                .build();

        result.setPurchasedNumbers(purchasedNumbers);

        return result;
    }

    private int determineRank(int matchCount, boolean bonusMatch) {
        if (matchCount == 6) {
            return 1; // 1등: 6개 일치
        } else if (matchCount == 5 && bonusMatch) {
            return 2; // 2등: 5개 + 보너스
        } else if (matchCount == 5) {
            return 3; // 3등: 5개 일치
        } else if (matchCount == 4) {
            return 4; // 4등: 4개 일치
        } else if (matchCount == 3) {
            return 5; // 5등: 3개 일치
        }
        return 0; // 꽝
    }

    @Transactional(readOnly = true)
    public List<LottoResultResponse> getResultsByDrawNo(Long drawNo) {
        List<LottoResultEntity> results = resultRepository.findByDrawNo(drawNo);

        return results.stream()
                .map(result -> LottoResultResponse.builder()
                        .id(result.getId())
                        .drawNo(result.getDrawNo())
                        .purchaseId(result.getPurchaseId())
                        .purchasedNumbers(result.getPurchasedNumberList())
                        .matchCount(result.getMatchCount())
                        .bonusMatch(result.getBonusMatch())
                        .rank(result.getRankValue())
                        .prizeAmount(result.getPrizeAmount())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public ResultStatisticsResponse getStatistics(Long drawNo) {
        Map<Integer, Long> rankCounts = new HashMap<>();
        Map<Integer, Long> rankPrizes = new HashMap<>();

        for (int rank = 1; rank <= 5; rank++) {
            Long count = resultRepository.countByDrawNoAndRank(drawNo, rank);
            Long prizeSum = resultRepository.sumPrizeAmountByDrawNoAndRank(drawNo, rank);

            rankCounts.put(rank, count != null ? count : 0L);
            rankPrizes.put(rank, prizeSum != null ? prizeSum : 0L);
        }

        int totalPurchases = resultRepository.findByDrawNo(drawNo).size();
        Long totalPrizeAmount = resultRepository.sumTotalPrizeAmountByDrawNo(drawNo);

        return ResultStatisticsResponse.builder()
                .drawNo(drawNo)
                .totalPurchases((long) totalPurchases)
                .rankCounts(rankCounts)
                .rankPrizes(rankPrizes)
                .totalPrizeAmount(totalPrizeAmount != null ? totalPrizeAmount : 0L)
                .build();
    }
}
