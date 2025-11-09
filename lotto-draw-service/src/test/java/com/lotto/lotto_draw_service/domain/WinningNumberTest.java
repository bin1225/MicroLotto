package com.lotto.lotto_draw_service.domain;

import com.lotto.lotto_draw_service.domain.WinningNumber.Builder;
import com.lotto.util.error.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WinningNumberTest {

    @Test
    void 유효한_당첨번호와_보너스번호면_정상적으로_생성된다() {
        // given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        int bonus = 7;

        // when
        WinningNumber result = new WinningNumber.Builder()
                .setWinningNumbers(numbers)
                .setBonusNumber(bonus)
                .build();

        // then
        assertThat(result.getNumbers()).containsExactly(1, 2, 3, 4, 5, 6);
        assertThat(result.getBonusNumber()).isEqualTo(7);
    }

    @ParameterizedTest
    @ValueSource(ints = {WinningNumber.SIZE - 1, WinningNumber.SIZE + 1})
    void 당첨번호가_로또_번호_개수와_다르면_예외가_발생한다(int count) {
        // given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7).subList(0, count);
        int bonus = 8;

        // when & then
        assertThatThrownBy(() -> new Builder()
                .setWinningNumbers(numbers)
                .setBonusNumber(bonus)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_WINNING_NUMBER_COUNT.getMessage(WinningNumber.SIZE));
    }

    @Test
    void 번호가_범위를_벗어나면_예외가_발생한다() {
        // given
        List<Integer> numbers = List.of(0, 2, 3, 4, 5, 6);
        int bonus = 7;

        // when & then
        assertThatThrownBy(() -> new WinningNumber.Builder()
                .setWinningNumbers(numbers)
                .setBonusNumber(bonus)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.INVALID_NUMBER_RANGE.getMessage(WinningNumber.MIN_NUMBER, WinningNumber.MAX_NUMBER));
    }

    @Test
    void 당첨번호에_중복이_있으면_예외가_발생한다() {
        // given
        List<Integer> numbers = List.of(1, 2, 3, 3, 4, 5);
        int bonus = 6;

        // when & then
        assertThatThrownBy(() -> new WinningNumber.Builder()
                .setWinningNumbers(numbers)
                .setBonusNumber(bonus)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.DUPLICATE_WINNING_NUMBER.getMessage());
    }

    @Test
    void 보너스번호가_당첨번호와_중복되면_예외가_발생한다() {
        // given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        int bonus = 6;

        // when & then
        assertThatThrownBy(() -> new WinningNumber.Builder()
                .setWinningNumbers(numbers)
                .setBonusNumber(bonus)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorMessage.DUPLICATE_BONUS_NUMBER.getMessage());
    }
}
