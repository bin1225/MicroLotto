package com.lotto.lotto_draw_service.service;


import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import com.lotto.lotto_draw_service.domain.repository.DrawRepository;
import com.lotto.util.error.ErrorMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DrawService {

    private final DrawRepository drawRepository;

    /**
     * 현재 진행 중인 회차 조회
     */
    public CurrentDrawResponse getCurrentDraw() {
        LocalDate today = LocalDate.now();

        Draw draw = drawRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .orElseThrow(() -> new IllegalStateException(
                        ErrorMessage.NOT_EXIST_CURRENT_DRAW.getMessage()));

        return CurrentDrawResponse.builder()
                .drawNo(draw.getDrawNo())
                .startDate(draw.getStartDate().toString())
                .endDate(draw.getEndDate().toString())
                .isClosed(draw.getIsClosed())
                .build();
    }
}
