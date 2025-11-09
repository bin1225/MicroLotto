package com.lotto.lotto_draw_service.application;


import com.lotto.lotto_draw_service.domain.WinningNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class WinningNumberGeneratorTest {

    @Test
    @DisplayName("랜덤으로 생성된 WinningNumber는 6개의 고유 번호와 1개의 보너스 번호를 가진다")
    void generate_validWinningNumber() {
        // when
        WinningNumber winningNumber = WinningNumberGenerator.generate();

        // then
        List<Integer> numbers = winningNumber.getNumbers();
        int bonus = winningNumber.getBonusNumber();

        assertThat(numbers).hasSize(WinningNumber.SIZE);
        assertThat(new HashSet<>(numbers)).hasSize(WinningNumber.SIZE);
        assertThat(numbers).allMatch(n -> n >= WinningNumber.MIN_NUMBER && n <= WinningNumber.MAX_NUMBER);
        assertThat(bonus)
                .isBetween(WinningNumber.MIN_NUMBER, WinningNumber.MAX_NUMBER)
                .isNotIn(numbers);
    }

    @RepeatedTest(5)
    @DisplayName("여러 번 생성해도 중복되지 않는 랜덤 번호를 생성한다")
    void generate_multipleTimes_randomness() {
        // when
        Set<List<Integer>> generatedSets = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            WinningNumber winning = WinningNumberGenerator.generate();
            generatedSets.add(winning.getNumbers());
        }

        // then
        assertThat(generatedSets.size()).isGreaterThan(1);
    }
}
