package com.lotto.lotto_purchase_service.domain.entity;

import com.lotto.util.error.ErrorMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class LottoNumbers {

    private static final int LOTTO_NUMBER_COUNT = 6;
    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 45;
    private static final String DELIMITER = ",";

    @Column(nullable = false, length = 50)
    private String numbers;

    public LottoNumbers(List<Integer> numbers) {
        validateNumbers(numbers);
        this.numbers = convertToString(numbers);
    }

    public List<Integer> toList() {
        return Arrays.stream(numbers.split(DELIMITER))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private String convertToString(List<Integer> numbers) {
        return numbers.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(DELIMITER));
    }

    private void validateNumbers(List<Integer> numbers) {
        validateCount(numbers);
        validateDuplication(numbers);
        validateRange(numbers);
    }

    private void validateCount(List<Integer> numbers) {
        if (numbers.size() != LOTTO_NUMBER_COUNT) {
            throw new IllegalArgumentException(
                    ErrorMessage.INVALID_LOTTO_NUMBER_COUNT.getMessage(LOTTO_NUMBER_COUNT)
            );
        }
    }

    private void validateDuplication(List<Integer> numbers) {
        if (numbers.stream().distinct().count() != LOTTO_NUMBER_COUNT) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATE_LOTTO_NUMBER.getMessage());
        }
    }

    private void validateRange(List<Integer> numbers) {
        if (numbers.stream().anyMatch(this::isOutOfRange)) {
            throw new IllegalArgumentException(
                    ErrorMessage.INVALID_NUMBER_RANGE.getMessage(MIN_NUMBER, MAX_NUMBER)
            );
        }
    }

    private boolean isOutOfRange(int number) {
        return number < MIN_NUMBER || number > MAX_NUMBER;
    }

    public static int getMinNumber() {
        return MIN_NUMBER;
    }

    public static int getMaxNumber() {
        return MAX_NUMBER;
    }

    public static int getCount() {
        return LOTTO_NUMBER_COUNT;
    }
}
