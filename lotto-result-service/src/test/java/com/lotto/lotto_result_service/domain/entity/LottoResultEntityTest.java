package com.lotto.lotto_result_service.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LottoResultEntityTest {

    @Test
    @DisplayName("구매 번호 리스트를 JSON 문자열로 저장한다")
    void setPurchasedNumbers() {
        // given
        LottoResultEntity entity = LottoResultEntity.builder()
                .drawNo(1L)
                .purchaseId(1L)
                .matchCount(6)
                .bonusMatch(false)
                .rankValue(1)
                .prizeAmount(2_000_000_000L)
                .build();

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

        // when
        entity.setPurchasedNumbers(numbers);

        // then
        assertThat(entity.getPurchasedNumbers()).isEqualTo("[1, 2, 3, 4, 5, 6]");
    }

    @Test
    @DisplayName("JSON 문자열을 구매 번호 리스트로 변환한다")
    void getPurchasedNumberList() {
        // given
        LottoResultEntity entity = LottoResultEntity.builder()
                .drawNo(1L)
                .purchaseId(1L)
                .purchasedNumbers("[1, 2, 3, 4, 5, 6]")
                .matchCount(6)
                .bonusMatch(false)
                .rankValue(1)
                .prizeAmount(2_000_000_000L)
                .build();

        // when
        List<Integer> numbers = entity.getPurchasedNumberList();

        // then
        assertThat(numbers).containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    @DisplayName("당첨 결과 엔티티를 생성한다")
    void createEntity() {
        // given & when
        LottoResultEntity entity = LottoResultEntity.builder()
                .drawNo(1L)
                .purchaseId(1L)
                .matchCount(6)
                .bonusMatch(false)
                .rankValue(1)
                .prizeAmount(2_000_000_000L)
                .build();

        entity.setPurchasedNumbers(List.of(1, 2, 3, 4, 5, 6));

        // then
        assertThat(entity.getDrawNo()).isEqualTo(1L);
        assertThat(entity.getPurchaseId()).isEqualTo(1L);
        assertThat(entity.getMatchCount()).isEqualTo(6);
        assertThat(entity.getBonusMatch()).isFalse();
        assertThat(entity.getRankValue()).isEqualTo(1);
        assertThat(entity.getPrizeAmount()).isEqualTo(2_000_000_000L);
        assertThat(entity.getPurchasedNumberList()).containsExactly(1, 2, 3, 4, 5, 6);
    }
}
