package com.lotto.lotto_draw_service.controller;


import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.application.service.DrawService;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/draws")
@RequiredArgsConstructor
public class DrawController {

    private final DrawService drawService;

    @GetMapping("/current")
    public ResponseEntity<CurrentDrawResponse> getCurrentDraw() {
        CurrentDrawResponse response = drawService.getCurrentDraw();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current/{delay}/{faultPercent}")
    public ResponseEntity<CurrentDrawResponse> getCurrentDrawWithDelayAndFault(
            @PathVariable int delay,
            @PathVariable int faultPercent) {
        log.info("delay: {}, faultPercent: {}", delay, faultPercent);
        CurrentDrawResponse response = drawService.getCurrentDrawWithFaultTolerance(delay, faultPercent);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rollover")
    public ResponseEntity<String> rolloverManual() {
        Draw draw = drawService.rolloverDailyDraw();
        return ResponseEntity.ok("Success: " + draw.getDrawNo() + " created");
    }
}
