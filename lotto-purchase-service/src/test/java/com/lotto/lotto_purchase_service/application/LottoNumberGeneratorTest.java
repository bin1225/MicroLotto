package com.lotto.lotto_purchase_service.application;

import com.lotto.lotto_purchase_service.domain.entity.LottoNumbers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LottoNumberGeneratorTest {

    @Test
    @DisplayName("로또 번호 6개가 1~45 범위 내에서 중복 없이 생성되어야 한다")
    void generate_validNumbers() {
        List<Integer> numbers = LottoNumberGenerator.generate();

        assertThat(numbers).hasSize(LottoNumbers.LOTTO_NUMBER_COUNT);
        assertThat(numbers).allMatch(n -> n >= LottoNumbers.MIN_NUMBER && n <= LottoNumbers.MAX_NUMBER);
        assertThat(numbers.stream().distinct().count()).isEqualTo(LottoNumbers.LOTTO_NUMBER_COUNT);
    }
}
