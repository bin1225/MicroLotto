package com.lotto.lotto_draw_service.application.service;


import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.client.ResultServiceClient;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import com.lotto.lotto_draw_service.domain.repository.DrawRepository;
import com.lotto.util.error.ErrorMessage;
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

    public Draw rolloverDailyDraw() {
        Long closedDrawNo = closeCurrentDraw();
        return createNewDraw();
    }

    /**
     * 현재 진행 중인 회차 종료 및 결과 계산 요청
     */
    private Long closeCurrentDraw() {
        LocalDate today = LocalDate.now();

        return drawRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today)
                .map(draw -> {
                    draw.close();
                    return draw.getDrawNo();
                })
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.NOT_EXIST_CURRENT_DRAW.getMessage()));
    }

    /**
     * 새 회차 생성
     */
    private Draw createNewDraw() {
        LocalDate today = LocalDate.now();
        Long nextDrawNo = drawRepository.findTopByOrderByDrawNoDesc()
                .map(d -> d.getDrawNo() + 1)
                .orElse(1L);

        Draw newDraw = Draw.builder()
                .drawNo(nextDrawNo)
                .startDate(today)
                .endDate(today.plusDays(1))
                .isClosed(false)
                .build();

        return drawRepository.save(newDraw);
    }


}
