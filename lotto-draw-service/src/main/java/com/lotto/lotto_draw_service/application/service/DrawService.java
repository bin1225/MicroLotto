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
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrawService {

    private final DrawRepository drawRepository;
    private final WinningNumberEntityRepository winningNumberEntityRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CurrentDrawResponse getCurrentDraw() {
        Draw draw = findCurrentDraw();
        return mapToResponse(draw);
    }

    public CurrentDrawResponse getCurrentDrawWithFaultTolerance(int delay, int faultPercent) {
        Draw draw = findCurrentDraw();
        simulateFaultTolerance(delay, faultPercent);
        return mapToResponse(draw);
    }

    @Transactional
    public Draw rolloverDailyDraw() {
        Long closedDrawNo = closeCurrentDraw();

        if (closedDrawNo != null) {
            generateAndSaveWinningNumber(closedDrawNo);
            publishDrawClosedEvent(closedDrawNo);
        }

        return createNewDraw();
    }

    private Draw findCurrentDraw() {
        return drawRepository.findTopByOrderByDrawNoDesc()
                .orElseThrow(() -> new IllegalStateException(
                        ErrorMessage.NOT_EXIST_CURRENT_DRAW.getMessage()
                ));
    }

    private void simulateFaultTolerance(int delay, int faultPercent) {
        throwErrorRandomly(faultPercent);
        applyDelay(delay);
    }

    private void throwErrorRandomly(int faultPercent) {
        if (faultPercent == 0) {
            return;
        }

        int randomNumber = Randoms.pickNumberInRange(1, 100);
        if (faultPercent >= randomNumber) {
            throw new RuntimeException("Simulated fault tolerance error");
        }
    }

    private void applyDelay(int delaySeconds) {
        if (delaySeconds <= 0) {
            return;
        }

        try {
            Thread.sleep(delaySeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Delay interrupted", e);
        }
    }

    private Long closeCurrentDraw() {
        return drawRepository.findTopByOrderByDrawNoDesc()
                .filter(Draw::isOpen)
                .map(this::closeDraw)
                .orElse(null);
    }

    private Long closeDraw(Draw draw) {
        draw.close();
        log.info("회차 종료: {}", draw.getDrawNo());
        return draw.getDrawNo();
    }

    private void generateAndSaveWinningNumber(Long drawNo) {
        WinningNumber winningNumber = WinningNumberGenerator.generate();
        WinningNumberEntity entity = WinningNumberEntity.from(
                winningNumber.getNumbers(),
                winningNumber.getBonusNumber(),
                drawNo
        );
        winningNumberEntityRepository.save(entity);
        log.info("당첨 번호 생성 - 회차: {}", drawNo);
    }

    private void publishDrawClosedEvent(Long drawNo) {
        eventPublisher.publishEvent(new DrawClosedEvent(drawNo));
    }

    private Draw createNewDraw() {
        Long nextDrawNo = calculateNextDrawNo();
        LocalDate today = LocalDate.now();

        Draw newDraw = Draw.createNew(nextDrawNo, today, today.plusDays(1));
        Draw saved = drawRepository.save(newDraw);

        log.info("신규 회차 생성: {}", saved.getDrawNo());
        return saved;
    }

    private Long calculateNextDrawNo() {
        return drawRepository.findTopByOrderByDrawNoDesc()
                .map(draw -> draw.getDrawNo() + 1)
                .orElse(1L);
    }

    private CurrentDrawResponse mapToResponse(Draw draw) {
        return CurrentDrawResponse.builder()
                .drawNo(draw.getDrawNo())
                .startDate(draw.getStartDate().toString())
                .endDate(draw.getEndDate().toString())
                .isClosed(draw.getIsClosed())
                .build();
    }
}
