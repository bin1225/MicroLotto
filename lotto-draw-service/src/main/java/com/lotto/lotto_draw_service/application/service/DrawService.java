package com.lotto.lotto_draw_service.application.service;


import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.application.WinningNumberGenerator;
import com.lotto.lotto_draw_service.client.ResultServiceClient;
import com.lotto.lotto_draw_service.domain.WinningNumber;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import com.lotto.lotto_draw_service.domain.entity.WinningNumberEntity;
import com.lotto.lotto_draw_service.domain.repository.DrawRepository;
import com.lotto.lotto_draw_service.domain.repository.WinningNumberEntityRepository;
import com.lotto.util.error.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DrawService {

    private final DrawRepository drawRepository;
    private final WinningNumberEntityRepository winningNumberEntityRepository;
    private final ResultServiceClient resultServiceClient;

    /**
     * 현재 진행 중인 회차 조회
     */
    public CurrentDrawResponse getCurrentDraw() {
        LocalDate today = LocalDate.now();

        Draw draw = drawRepository.findTopByOrderByDrawNoDesc()
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
        //현재 회차 종료
        Long closedDrawNo = closeCurrentDraw();

        if (closedDrawNo != null) {
            //당첨 번호 생성
            WinningNumber winningNumber = WinningNumberGenerator.generate();
            WinningNumberEntity winningNumberEntity = WinningNumberEntity.from(winningNumber.getNumbers(),
                    winningNumber.getBonusNumber(), closedDrawNo);
            winningNumberEntityRepository.save(winningNumberEntity);

            //결과 계산 요청
            //resultServiceClient.requestResultCalculation(closedDrawNo);
        }

        //새로운 회차 생성 및 반환
        return createNewDraw();
    }

    /**
     * 현재 진행 중인 회차 종료 및 결과 계산 요청
     */
    private Long closeCurrentDraw() {
        LocalDate today = LocalDate.now();

        return drawRepository.findTopByOrderByDrawNoDesc()
                .map(draw -> {
                    draw.close();
                    return draw.getDrawNo();
                }).orElse(null);
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
