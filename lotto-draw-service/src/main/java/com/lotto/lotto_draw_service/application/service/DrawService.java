package com.lotto.lotto_draw_service.application.service;


import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.application.WinningNumberGenerator;
import com.lotto.lotto_draw_service.application.event.DrawClosedEvent;
import com.lotto.lotto_draw_service.domain.WinningNumber;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import com.lotto.lotto_draw_service.domain.entity.WinningNumberEntity;
import com.lotto.lotto_draw_service.domain.repository.DrawRepository;
import com.lotto.lotto_draw_service.domain.repository.WinningNumberEntityRepository;
import com.lotto.util.Randoms;
import com.lotto.util.error.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrawService {

    private final DrawRepository drawRepository;
    private final WinningNumberEntityRepository winningNumberEntityRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 현재 진행 중인 회차 조회
     */
    @Transactional(readOnly = true)
    public CurrentDrawResponse getCurrentDraw() {
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

    @Transactional(readOnly = true)
    public CurrentDrawResponse getCurrentDraw(int delay, int faultPercent) {
        Draw draw = drawRepository.findTopByOrderByDrawNoDesc()
                .map(d-> throwErrorIfBadLuck(d, faultPercent))
                .orElseThrow(() -> new IllegalStateException(
                        ErrorMessage.NOT_EXIST_CURRENT_DRAW.getMessage()));

        try {
            Thread.sleep(delay * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return CurrentDrawResponse.builder()
                .drawNo(draw.getDrawNo())
                .startDate(draw.getStartDate().toString())
                .endDate(draw.getEndDate().toString())
                .isClosed(draw.getIsClosed())
                .build();
    }

    private Draw throwErrorIfBadLuck(Draw draw, int faultPercent) {
        if(faultPercent==0) return draw;

        int randomNumber = Randoms.pickNumberInRange(1, 100);
        if(faultPercent<randomNumber) {
            log.info("Good luck, no Error {} < {}", faultPercent, randomNumber);
        }
        else {
            throw new RuntimeException("Bad luck, Error " + faultPercent + " >= " + randomNumber);
        }

        return draw;
    }
    /**
     * 회차 롤오버
     */
    @Transactional
    public Draw rolloverDailyDraw() {
        // 1. 현재 회차 종료
        Long closedDrawNo = closeCurrentDraw();

        // 2. 당첨번호 생성 및 저장
        if (closedDrawNo != null) {
            generateAndSaveWinningNumber(closedDrawNo);

            // 3. 트랜잭션 커밋 후 리스너 실행
            eventPublisher.publishEvent(new DrawClosedEvent(closedDrawNo));
        }

        // 4. 새로운 회차 생성
        return createNewDraw();
    }

    /**
     * 현재 회차 종료
     */
    private Long closeCurrentDraw() {
        return drawRepository.findTopByOrderByDrawNoDesc()
                .map(draw -> {
                    draw.close();
                    log.info("Closed draw: {}", draw.getDrawNo());
                    return draw.getDrawNo();
                }).orElse(null);
    }

    /**
     * 당첨번호 생성 및 저장
     */
    private void generateAndSaveWinningNumber(Long drawNo) {
        WinningNumber winningNumber = WinningNumberGenerator.generate();
        WinningNumberEntity winningNumberEntity = WinningNumberEntity.from(
                winningNumber.getNumbers(),
                winningNumber.getBonusNumber(),
                drawNo
        );

        winningNumberEntityRepository.save(winningNumberEntity);
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

        Draw saved = drawRepository.save(newDraw);
        log.info("Created new draw: {}", saved.getDrawNo());
        return saved;
    }
}
