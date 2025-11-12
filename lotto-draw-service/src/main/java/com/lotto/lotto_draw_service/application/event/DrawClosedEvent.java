package com.lotto.lotto_draw_service.application.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회차 종료 이벤트
 * 당첨 번호 저장 완료 후 결과 계산을 요청하기 위한 이벤트
 */
@Getter
@RequiredArgsConstructor
public class DrawClosedEvent {
    private final Long drawNo;
}
