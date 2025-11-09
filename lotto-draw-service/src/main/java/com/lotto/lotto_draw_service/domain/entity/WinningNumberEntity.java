package com.lotto.lotto_draw_service.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinningNumberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drawNo;

    @Column(nullable = false, length = 100)
    private String winningNumbers;

    @Column(nullable = false)
    private Integer bonusNumber;

    public static WinningNumberEntity from(List<Integer> numbers, int bonusNumber, Long drawNo) {
        return WinningNumberEntity.builder()
                .drawNo(drawNo)
                .winningNumbers(numbers.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .bonusNumber(bonusNumber)
                .build();
    }

    public List<Integer> getWinningNumberList() {
        return Arrays.stream(winningNumbers.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}

