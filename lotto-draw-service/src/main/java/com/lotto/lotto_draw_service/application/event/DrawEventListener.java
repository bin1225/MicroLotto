package com.lotto.lotto_draw_service.application.event;

import com.lotto.lotto_draw_service.client.ResultServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 당첨 번호 저장 후 생성되는 이벤트 리스너
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DrawEventListener {

    private final ResultServiceClient resultServiceClient;

    /**
     * 당첨 번호 저장 후 결과 계산 요청
     * TransactionPhase.AFTER_COMMIT: 트랜잭션 커밋 후 실행
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDrawClosed(DrawClosedEvent event) {
        Long drawNo = event.getDrawNo();
        
        try {
            log.info("DrawClosedEvent for draw: {}", drawNo);
            resultServiceClient.requestResultCalculation(drawNo);
            log.info("Successfully requested result calculation for draw: {}", drawNo);
        } catch (Exception e) {
            log.error("Failed to request result calculation for draw: {}", drawNo, e);
        }
    }
}
