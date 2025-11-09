package com.lotto.lotto_purchase_service.domain.entity;

import com.lotto.util.error.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoNumbersTest {

    @Test
    @DisplayName("정상적인 6개 숫자가 입력되면 정렬된 문자열로 저장된다")
    void validLottoNumbers() {
        List<Integer> input = Arrays.asList(8, 1, 30, 14, 22, 3);
        LottoNumbers lottoNumbers = new LottoNumbers(input);

        assertThat(lottoNumbers.getNumbers()).isEqualTo("1,3,8,14,22,30");
        assertThat(lottoNumbers.getNumberList()).containsExactly(1, 3, 8, 14, 22, 30);
    }

    @Test
    @DisplayName("숫자가 6개가 아니면 예외가 발생한다")
    void invalidCount() {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5); // 5개

        assertThatThrownBy(() -> new LottoNumbers(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_LOTTO_NUMBER_COUNT.getMessage(LottoNumbers.LOTTO_NUMBER_COUNT));
    }

    @Test
    @DisplayName("중복된 숫자가 있으면 예외가 발생한다")
    void duplicateNumbers() {
        List<Integer> input = Arrays.asList(1, 2, 3, 3, 4, 5);

        assertThatThrownBy(() -> new LottoNumbers(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.DUPLICATE_LOTTO_NUMBER.getMessage());
    }

    @Test
    @DisplayName("1~45 범위를 벗어난 숫자가 있으면 예외가 발생한다")
    void invalidRange() {
        List<Integer> input = Arrays.asList(0, 2, 3, 4, 5, 6);

        assertThatThrownBy(() -> new LottoNumbers(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.INVALID_NUMBER_RANGE.getMessage(LottoNumbers.MIN_NUMBER, LottoNumbers.MAX_NUMBER));
    }
}
