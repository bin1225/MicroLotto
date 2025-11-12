package com.lotto.lotto_result_service.application.service;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import com.lotto.lotto_api.result.dto.LottoResultResponse;
import com.lotto.lotto_api.result.dto.ResultStatisticsResponse;
import com.lotto.lotto_result_service.client.DrawServiceClient;
import com.lotto.lotto_result_service.client.PurchaseServiceClient;
import com.lotto.lotto_result_service.domain.entity.LottoResultEntity;
import com.lotto.lotto_result_service.domain.repository.LottoResultEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
class ResultCalculationServiceTest {

    @Mock
    private DrawServiceClient drawServiceClient;

    @Mock
    private PurchaseServiceClient purchaseServiceClient;

    @Mock
    private LottoResultEntityRepository resultRepository;

    @InjectMocks
    private ResultCalculationService resultCalculationService;

    private WinningNumberResponse winningNumber;
    private List<PurchaseResponse> purchases;

    @BeforeEach
    void setUp() {
        // 당첨 번호: [1, 2, 3, 4, 5, 6], 보너스: 7
        winningNumber = WinningNumberResponse.builder()
                .drawNo(1L)
                .winningNumbers(List.of(1, 2, 3, 4, 5, 6))
                .bonusNumber(7)
                .build();

        // 다양한 등수의 구매 내역 생성
        purchases = List.of(
                // 1등: 6개 일치
                createPurchase(1L, 1L, List.of(1, 2, 3, 4, 5, 6)),
                // 2등: 5개 + 보너스
                createPurchase(2L, 1L, List.of(1, 2, 3, 4, 5, 7)),
                // 3등: 5개 일치
                createPurchase(3L, 1L, List.of(1, 2, 3, 4, 5, 8)),
                // 4등: 4개 일치
                createPurchase(4L, 1L, List.of(1, 2, 3, 4, 8, 9)),
                // 5등: 3개 일치
                createPurchase(5L, 1L, List.of(1, 2, 3, 8, 9, 10)),
                // 꽝: 2개 일치
                createPurchase(6L, 1L, List.of(1, 2, 8, 9, 10, 11))
        );
    }

    @Test
    @DisplayName("1등 - 6개 숫자가 모두 일치하면 1등이다")
    void calculateResults_FirstPrize() {
        // given
        List<PurchaseResponse> firstPrizePurchases = List.of(
                createPurchase(1L, 1L, List.of(1, 2, 3, 4, 5, 6))
        );

        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(firstPrizePurchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) -> {
            LottoResultEntity result = results.getFirst();
            return result.getRankValue() == 1 &&
                   result.getMatchCount() == 6 &&
                   result.getPrizeAmount() == 2_000_000_000L;
        }));
    }

    @Test
    @DisplayName("2등 - 5개 숫자 + 보너스 번호가 일치하면 2등이다")
    void calculateResults_SecondPrize() {
        // given
        List<PurchaseResponse> secondPrizePurchases = List.of(
                createPurchase(1L, 1L, List.of(1, 2, 3, 4, 5, 7))
        );

        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(secondPrizePurchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) -> {
            LottoResultEntity result = results.getFirst();
            return result.getRankValue() == 2 &&
                   result.getMatchCount() == 5 &&
                   result.getBonusMatch() &&
                   result.getPrizeAmount() == 30_000_000L;
        }));
    }

    @Test
    @DisplayName("3등 - 5개 숫자만 일치하면 3등이다")
    void calculateResults_ThirdPrize() {
        // given
        List<PurchaseResponse> thirdPrizePurchases = List.of(
                createPurchase(1L, 1L, List.of(1, 2, 3, 4, 5, 8))
        );

        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(thirdPrizePurchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) -> {
            LottoResultEntity result = results.getFirst();
            return result.getRankValue() == 3 &&
                   result.getMatchCount() == 5 &&
                   !result.getBonusMatch() &&
                   result.getPrizeAmount() == 1_500_000L;
        }));
    }

    @Test
    @DisplayName("4등 - 4개 숫자가 일치하면 4등이다")
    void calculateResults_FourthPrize() {
        // given
        List<PurchaseResponse> fourthPrizePurchases = List.of(
                createPurchase(1L, 1L, List.of(1, 2, 3, 4, 8, 9))
        );

        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(fourthPrizePurchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) -> {
            LottoResultEntity result = results.getFirst();
            return result.getRankValue() == 4 &&
                   result.getMatchCount() == 4 &&
                   result.getPrizeAmount() == 50_000L;
        }));
    }

    @Test
    @DisplayName("5등 - 3개 숫자가 일치하면 5등이다")
    void calculateResults_FifthPrize() {
        // given
        List<PurchaseResponse> fifthPrizePurchases = List.of(
                createPurchase(1L, 1L, List.of(1, 2, 3, 8, 9, 10))
        );

        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(fifthPrizePurchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) -> {
            LottoResultEntity result = results.getFirst();
            return result.getRankValue() == 5 &&
                   result.getMatchCount() == 3 &&
                   result.getPrizeAmount() == 5_000L;
        }));
    }

    @Test
    @DisplayName("꽝 - 2개 이하 숫자가 일치하면 꽝이다")
    void calculateResults_NoPrize() {
        // given
        List<PurchaseResponse> noPrizePurchases = List.of(
                createPurchase(1L, 1L, List.of(1, 2, 8, 9, 10, 11))
        );

        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(noPrizePurchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) -> {
            LottoResultEntity result = results.getFirst();
            return result.getRankValue() == 0 &&
                   result.getMatchCount() == 2 &&
                   result.getPrizeAmount() == 0L;
        }));
    }

    @Test
    @DisplayName("여러 구매 내역에 대해 결과를 일괄 계산한다")
    void calculateResults_MultipleTickets() {
        // given
        when(drawServiceClient.getWinningNumber(1L)).thenReturn(winningNumber);
        when(purchaseServiceClient.getPurchasesByDrawNo(1L)).thenReturn(purchases);

        // when
        resultCalculationService.calculateResults(1L);

        // then
        verify(resultRepository).saveAll(argThat((List<LottoResultEntity> results) ->
            results.size() == 6
        ));
    }

    @Test
    @DisplayName("회차별 당첨 결과를 조회한다")
    void getResultsByDrawNo() {
        // given
        Long drawNo = 1L;
        List<LottoResultEntity> mockResults = List.of(
                createResultEntity(1L, 1L, 1L, List.of(1, 2, 3, 4, 5, 6), 6, false, 1, 2_000_000_000L),
                createResultEntity(2L, 1L, 2L, List.of(1, 2, 3, 4, 5, 7), 5, true, 2, 30_000_000L)
        );

        when(resultRepository.findByDrawNo(drawNo)).thenReturn(mockResults);

        // when
        List<LottoResultResponse> results = resultCalculationService.getResultsByDrawNo(drawNo);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getRankValue()).isEqualTo(1);
        assertThat(results.get(1).getRankValue()).isEqualTo(2);
    }

    @Test
    @DisplayName("회차별 당첨 통계를 조회한다")
    void getStatistics() {
        // given
        Long drawNo = 1L;
        List<LottoResultEntity> mockResults = List.of(
                createResultEntity(1L, 1L, 1L, List.of(1, 2, 3, 4, 5, 6), 6, false, 1, 2_000_000_000L),
                createResultEntity(2L, 1L, 2L, List.of(1, 2, 3, 4, 5, 7), 5, true, 2, 30_000_000L),
                createResultEntity(3L, 1L, 3L, List.of(1, 2, 3, 8, 9, 10), 3, false, 5, 5_000L)
        );

        when(resultRepository.findByDrawNo(drawNo)).thenReturn(mockResults);
        when(resultRepository.countByDrawNoAndRank(drawNo, 1)).thenReturn(1L);
        when(resultRepository.countByDrawNoAndRank(drawNo, 2)).thenReturn(1L);
        when(resultRepository.countByDrawNoAndRank(drawNo, 5)).thenReturn(1L);
        when(resultRepository.sumPrizeAmountByDrawNoAndRank(drawNo, 1)).thenReturn(2_000_000_000L);
        when(resultRepository.sumPrizeAmountByDrawNoAndRank(drawNo, 2)).thenReturn(30_000_000L);
        when(resultRepository.sumPrizeAmountByDrawNoAndRank(drawNo, 5)).thenReturn(5_000L);
        when(resultRepository.sumTotalPrizeAmountByDrawNo(drawNo)).thenReturn(2_030_005_000L);

        // when
        ResultStatisticsResponse statistics = resultCalculationService.getStatistics(drawNo);

        // then
        assertThat(statistics.getDrawNo()).isEqualTo(drawNo);
        assertThat(statistics.getTotalPurchases()).isEqualTo(3L);
        assertThat(statistics.getRankCounts().get(1)).isEqualTo(1L);
        assertThat(statistics.getRankCounts().get(2)).isEqualTo(1L);
        assertThat(statistics.getRankPrizes().get(1)).isEqualTo(2_000_000_000L);
        assertThat(statistics.getTotalPrizeAmount()).isEqualTo(2_030_005_000L);
    }

    private PurchaseResponse createPurchase(Long id, Long drawNo, List<Integer> numbers) {
        return PurchaseResponse.builder()
                .id(id)
                .drawNo(drawNo)
                .numbers(numbers)
                .purchasedAt(LocalDateTime.now())
                .build();
    }

    private LottoResultEntity createResultEntity(
            Long id, Long drawNo, Long purchaseId, List<Integer> numbers,
            int matchCount, boolean bonusMatch, int rankValue, long prizeAmount
    ) {
        LottoResultEntity entity = LottoResultEntity.builder()
                .id(id)
                .drawNo(drawNo)
                .purchaseId(purchaseId)
                .matchCount(matchCount)
                .bonusMatch(bonusMatch)
                .rankValue(rankValue)
                .prizeAmount(prizeAmount)
                .build();
        entity.setPurchasedNumbers(numbers);
        return entity;
    }
}
