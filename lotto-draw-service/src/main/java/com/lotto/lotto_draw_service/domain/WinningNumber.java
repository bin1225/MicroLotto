package com.lotto.lotto_draw_service.domain;

import static com.lotto.util.error.ErrorMessage.*;

import java.util.HashSet;
import java.util.List;
import lombok.Getter;

@Getter
public class WinningNumber {

    public static final int SIZE = 6;
    public static final int MIN_NUMBER = 1;
    public static final int MAX_NUMBER = 45;

    private final List<Integer> numbers;
    private final int bonusNumber;

    private WinningNumber(List<Integer> numbers, int bonusNumber) {
        this.numbers = numbers;
        this.bonusNumber = bonusNumber;
    }

    public static class Builder {
        private List<Integer> winningNumbers;
        private Integer bonusNumber;

        public Builder setWinningNumbers(List<Integer> numbers) {
            validateWinningNumbers(numbers);
            this.winningNumbers = numbers;
            return this;
        }

        public Builder setBonusNumber(int bonusNumber) {
            if (winningNumbers == null) {
                throw new IllegalStateException(WINNING_NUMBER_NOT_SET.getMessage());
            }
            validateBonusNumber(bonusNumber, winningNumbers);
            this.bonusNumber = bonusNumber;
            return this;
        }

        public WinningNumber build() {
            if (winningNumbers == null || bonusNumber == null) {
                throw new IllegalStateException(INCOMPLETE_WINNING_NUMBER.getMessage());
            }
            return new WinningNumber(winningNumbers, bonusNumber);
        }
    }

    public static void validateWinningNumbers(List<Integer> numbers) {
        validateSize(numbers);
        numbers.forEach(WinningNumber::validateNumberRange);
        validateNoDuplicate(numbers);
    }

    private static void validateBonusNumber(int bonusNumber, List<Integer> winningNumbers) {
        validateNumberRange(bonusNumber);
        if (winningNumbers.contains(bonusNumber)) {
            throw new IllegalArgumentException(DUPLICATE_BONUS_NUMBER.getMessage());
        }
    }

    private static void validateSize(List<Integer> numbers) {
        if (numbers.size() != SIZE) {
            throw new IllegalArgumentException(INVALID_WINNING_NUMBER_COUNT.getMessage(SIZE));
        }
    }

    private static void validateNoDuplicate(List<Integer> numbers) {
        if (new HashSet<>(numbers).size() != numbers.size()) {
            throw new IllegalArgumentException(DUPLICATE_WINNING_NUMBER.getMessage());
        }
    }

    private static void validateNumberRange(int number) {
        if (number < MIN_NUMBER || number > MAX_NUMBER) {
            throw new IllegalArgumentException(INVALID_NUMBER_RANGE.getMessage(MIN_NUMBER, MAX_NUMBER));
        }
    }
}
