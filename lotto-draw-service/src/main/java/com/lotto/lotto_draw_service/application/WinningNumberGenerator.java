package com.lotto.lotto_draw_service.application;

import com.lotto.lotto_draw_service.domain.WinningNumber;
import com.lotto.util.Randoms;
import java.util.List;

public final class WinningNumberGenerator {

    private WinningNumberGenerator() {
        throw new AssertionError("인스턴스 생성 불가");
    }

    public static WinningNumber generate() {
        List<Integer> numbers = generateMainNumbers();
        int bonusNumber = generateBonusNumber(numbers);

        return WinningNumber.builder()
                .winningNumbers(numbers)
                .bonusNumber(bonusNumber)
                .build();
    }

    private static List<Integer> generateMainNumbers() {
        return Randoms.pickUniqueNumbersInRange(
                WinningNumber.MIN_NUMBER,
                WinningNumber.MAX_NUMBER,
                WinningNumber.SIZE
        );
    }

    private static int generateBonusNumber(List<Integer> existingNumbers) {
        int bonusNumber;
        do {
            bonusNumber = Randoms.pickNumberInRange(
                    WinningNumber.MIN_NUMBER,
                    WinningNumber.MAX_NUMBER
            );
        } while (existingNumbers.contains(bonusNumber));
        return bonusNumber;
    }
}
