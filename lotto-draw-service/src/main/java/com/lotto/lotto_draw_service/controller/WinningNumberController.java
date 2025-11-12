package com.lotto.lotto_draw_service.controller;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import com.lotto.lotto_draw_service.application.service.WinningNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/winning-numbers")
@RequiredArgsConstructor
public class WinningNumberController {

    private final WinningNumberService winningNumberService;

    @GetMapping("/{drawNo}")
    public ResponseEntity<WinningNumberResponse> getWinningNumberByDrawNo(
            @PathVariable Long drawNo) {
        WinningNumberResponse response = winningNumberService.getWinningNumberByDrawNo(drawNo);
        return ResponseEntity.ok(response);
    }
}
