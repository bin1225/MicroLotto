package com.lotto.lotto_draw_service.application.service;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import com.lotto.lotto_draw_service.domain.entity.WinningNumberEntity;
import com.lotto.lotto_draw_service.domain.repository.WinningNumberEntityRepository;
import com.lotto.util.error.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WinningNumberService {

    private final WinningNumberEntityRepository winningNumberEntityRepository;

    public WinningNumberResponse getWinningNumberByDrawNo(Long drawNo) {
        WinningNumberEntity winningNumber = winningNumberEntityRepository.findByDrawNo(drawNo)
                .orElseThrow(() -> new IllegalArgumentException(
                        ErrorMessage.NOT_EXIST_WINNING_NUMBER.getMessage()));

        return WinningNumberResponse.builder()
                .drawNo(winningNumber.getDrawNo())
                .winningNumbers(winningNumber.getWinningNumberList())
                .bonusNumber(winningNumber.getBonusNumber())
                .build();
    }
}
