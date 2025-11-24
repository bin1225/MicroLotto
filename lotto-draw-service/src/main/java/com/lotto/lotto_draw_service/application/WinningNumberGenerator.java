package com.lotto.lotto_draw_service.application;

import static com.lotto.lotto_draw_service.domain.WinningNumber.*;

import com.lotto.lotto_draw_service.domain.WinningNumber;
import com.lotto.util.Randoms;
import java.util.List;

public class WinningNumberGenerator {

    public static WinningNumber generate() {
        List<Integer> numbers = Randoms.pickUniqueNumbersInRange(MIN_NUMBER, MAX_NUMBER, SIZE);
        int bonusNumber = generateBonusNumber(numbers);

        return WinningNumber.builder()
                .winningNumbers(numbers)
                .bonusNumber(bonusNumber)
                .build();
    }

    private static int generateBonusNumber(List<Integer> existingNumbers) {
        int bonus;
        do {
            bonus = Randoms.pickNumberInRange(MIN_NUMBER, MAX_NUMBER);
        } while (existingNumbers.contains(bonus));
        return bonus;
    }
}
