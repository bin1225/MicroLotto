package com.lotto.lotto_draw_service.service;

import com.lotto.lotto_draw_service.application.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DrawService {

    /**
     * 현재 진행 중인 회차 조회
     */
    public CurrentDrawResponse getCurrentDraw() {
        Draw mockDraw = Draw.builder()
                .id(1L)
                .drawNo(1150L)  // 현재 회차
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(1))
                .isClosed(false)
                .build();

        return CurrentDrawResponse.from(mockDraw);
    }
}
