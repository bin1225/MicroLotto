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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Integer> winningNumbers;
        private Integer bonusNumber;

        public Builder winningNumbers(List<Integer> numbers) {
            validateWinningNumbers(numbers);
            this.winningNumbers = numbers;
            return this;
        }

        public Builder bonusNumber(int bonusNumber) {
            validateBuilderState();
            validateBonusNumber(bonusNumber, winningNumbers);
            this.bonusNumber = bonusNumber;
            return this;
        }

        public WinningNumber build() {
            validateComplete();
            return new WinningNumber(winningNumbers, bonusNumber);
        }

        private void validateBuilderState() {
            if (winningNumbers == null) {
                throw new IllegalStateException(WINNING_NUMBER_NOT_SET.getMessage());
            }
        }

        private void validateComplete() {
            if (winningNumbers == null || bonusNumber == null) {
                throw new IllegalStateException(INCOMPLETE_WINNING_NUMBER.getMessage());
            }
        }
    }

    private static void validateWinningNumbers(List<Integer> numbers) {
        validateSize(numbers);
        validateRange(numbers);
        validateDuplication(numbers);
    }

    private static void validateBonusNumber(int bonusNumber, List<Integer> winningNumbers) {
        validateNumberRange(bonusNumber);
        validateBonusNotDuplicate(bonusNumber, winningNumbers);
    }

    private static void validateSize(List<Integer> numbers) {
        if (numbers.size() != SIZE) {
            throw new IllegalArgumentException(INVALID_WINNING_NUMBER_COUNT.getMessage(SIZE));
        }
    }

    private static void validateRange(List<Integer> numbers) {
        numbers.forEach(WinningNumber::validateNumberRange);
    }

    private static void validateDuplication(List<Integer> numbers) {
        if (hasDuplicates(numbers)) {
            throw new IllegalArgumentException(DUPLICATE_WINNING_NUMBER.getMessage());
        }
    }

    private static boolean hasDuplicates(List<Integer> numbers) {
        return new HashSet<>(numbers).size() != numbers.size();
    }

    private static void validateBonusNotDuplicate(int bonusNumber, List<Integer> winningNumbers) {
        if (winningNumbers.contains(bonusNumber)) {
            throw new IllegalArgumentException(DUPLICATE_BONUS_NUMBER.getMessage());
        }
    }

    private static void validateNumberRange(int number) {
        if (isOutOfRange(number)) {
            throw new IllegalArgumentException(INVALID_NUMBER_RANGE.getMessage(MIN_NUMBER, MAX_NUMBER));
        }
    }

    private static boolean isOutOfRange(int number) {
        return number < MIN_NUMBER || number > MAX_NUMBER;
    }
}
