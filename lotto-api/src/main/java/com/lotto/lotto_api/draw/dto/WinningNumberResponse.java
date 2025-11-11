package com.lotto.lotto_api.draw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinningNumberResponse {
    private Long drawNo;
    private List<Integer> winningNumbers;
    private Integer bonusNumber;
}
