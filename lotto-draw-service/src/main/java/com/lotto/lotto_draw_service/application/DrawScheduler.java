package com.lotto.lotto_draw_service.application;

import com.lotto.lotto_draw_service.application.service.DrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrawScheduler {

    private final DrawService drawService;

    @Scheduled(cron = "0 0 0 * * *")
    public void runDailyRollover() {
        drawService.rolloverDailyDraw();
    }
}
