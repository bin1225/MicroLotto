package com.lotto.lotto_result_service.application.service;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import com.lotto.lotto_api.result.dto.LottoResultResponse;
import com.lotto.lotto_api.result.dto.ResultStatisticsResponse;
import com.lotto.lotto_result_service.client.DrawServiceClient;
import com.lotto.lotto_result_service.client.PurchaseServiceClient;
import com.lotto.lotto_result_service.domain.entity.LottoResultEntity;
import com.lotto.lotto_result_service.domain.repository.LottoResultEntityRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultCalculationService {

    private static final Map<Integer, Long> PRIZE_AMOUNTS = Map.of(
            1, 2_000_000_000L,
            2, 30_000_000L,
            3, 1_500_000L,
            4, 50_000L,
            5, 5_000L
    );

    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 5;
    private static final int NO_PRIZE_RANK = 0;

    private final DrawServiceClient drawServiceClient;
    private final PurchaseServiceClient purchaseServiceClient;
    private final LottoResultEntityRepository resultRepository;

    @Transactional
    public void calculateResults(Long drawNo) {
        WinningNumberResponse winningNumber = fetchWinningNumber(drawNo);
        List<PurchaseResponse> purchases = fetchPurchases(drawNo);

        List<LottoResultEntity> results = calculateAllResults(drawNo, purchases, winningNumber);
        resultRepository.saveAll(results);

        log.info("회차 {} 결과 계산 완료 - 총 {}건", drawNo, results.size());
    }

    public List<LottoResultResponse> getResultsByDrawNo(Long drawNo) {
        List<LottoResultEntity> results = resultRepository.findByDrawNo(drawNo);
        return results.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ResultStatisticsResponse getStatistics(Long drawNo) {
        Map<Integer, Long> rankCounts = calculateRankCounts(drawNo);
        Map<Integer, Long> rankPrizes = calculateRankPrizes(drawNo);

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

    private WinningNumberResponse fetchWinningNumber(Long drawNo) {
        return drawServiceClient.getWinningNumber(drawNo);
    }

    private List<PurchaseResponse> fetchPurchases(Long drawNo) {
        return purchaseServiceClient.getPurchasesByDrawNo(drawNo);
    }

    private List<LottoResultEntity> calculateAllResults(
            Long drawNo,
            List<PurchaseResponse> purchases,
            WinningNumberResponse winningNumber
    ) {
        return purchases.stream()
                .map(purchase -> calculateSingleResult(drawNo, purchase, winningNumber))
                .collect(Collectors.toList());
    }

    private LottoResultEntity calculateSingleResult(
            Long drawNo,
            PurchaseResponse purchase,
            WinningNumberResponse winningNumber
    ) {
        List<Integer> purchasedNumbers = purchase.getNumbers();
        List<Integer> winningNumbers = winningNumber.getWinningNumbers();
        Integer bonusNumber = winningNumber.getBonusNumber();

        int matchCount = calculateMatchCount(purchasedNumbers, winningNumbers);
        boolean bonusMatch = purchasedNumbers.contains(bonusNumber);
        int rank = determineRank(matchCount, bonusMatch);
        long prizeAmount = getPrizeAmount(rank);

        return LottoResultEntity.builder()
                .drawNo(drawNo)
                .purchaseId(purchase.getId())
                .purchasedNumbers(purchasedNumbers)
                .matchCount(matchCount)
                .bonusMatch(bonusMatch)
                .rankValue(rank)
                .prizeAmount(prizeAmount)
                .build();
    }

    private int calculateMatchCount(List<Integer> purchasedNumbers, List<Integer> winningNumbers) {
        return (int) purchasedNumbers.stream()
                .filter(winningNumbers::contains)
                .count();
    }

    private int determineRank(int matchCount, boolean bonusMatch) {
        if (matchCount == 6) {
            return 1;
        }
        if (matchCount == 5 && bonusMatch) {
            return 2;
        }
        if (matchCount == 5) {
            return 3;
        }
        if (matchCount == 4) {
            return 4;
        }
        if (matchCount == 3) {
            return 5;
        }
        return NO_PRIZE_RANK;
    }

    private long getPrizeAmount(int rank) {
        return PRIZE_AMOUNTS.getOrDefault(rank, 0L);
    }

    private Map<Integer, Long> calculateRankCounts(Long drawNo) {
        Map<Integer, Long> rankCounts = new HashMap<>();
        for (int rank = MIN_RANK; rank <= MAX_RANK; rank++) {
            Long count = resultRepository.countByDrawNoAndRank(drawNo, rank);
            rankCounts.put(rank, count != null ? count : 0L);
        }
        return rankCounts;
    }

    private Map<Integer, Long> calculateRankPrizes(Long drawNo) {
        Map<Integer, Long> rankPrizes = new HashMap<>();
        for (int rank = MIN_RANK; rank <= MAX_RANK; rank++) {
            Long prizeSum = resultRepository.sumPrizeAmountByDrawNoAndRank(drawNo, rank);
            rankPrizes.put(rank, prizeSum != null ? prizeSum : 0L);
        }
        return rankPrizes;
    }

    private LottoResultResponse mapToResponse(LottoResultEntity result) {
        return LottoResultResponse.builder()
                .id(result.getId())
                .drawNo(result.getDrawNo())
                .purchaseId(result.getPurchaseId())
                .purchasedNumbers(result.getPurchasedNumberList())
                .matchCount(result.getMatchCount())
                .bonusMatch(result.getBonusMatch())
                .rankValue(result.getRankValue())
                .prizeAmount(result.getPrizeAmount())
                .build();
    }
}
