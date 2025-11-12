package com.lotto.lotto_result_service.controller;

import com.lotto.lotto_api.result.dto.LottoResultResponse;
import com.lotto.lotto_api.result.dto.ResultStatisticsResponse;
import com.lotto.lotto_result_service.application.service.ResultCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultCalculationService resultCalculationService;

    /**
     * 특정 회차의 당첨 결과 계산
     */
    @PostMapping("/calculate/{drawNo}")
    public ResponseEntity<Void> calculateResults(@PathVariable Long drawNo) {
        log.info("Received result calculation request for draw: {}", drawNo);
        resultCalculationService.calculateResults(drawNo);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 회차의 당첨 결과 조회
     */
    @GetMapping("/draw/{drawNo}")
    public ResponseEntity<List<LottoResultResponse>> getResultsByDrawNo(@PathVariable Long drawNo) {
        log.info("Fetching results for draw: {}", drawNo);
        List<LottoResultResponse> results = resultCalculationService.getResultsByDrawNo(drawNo);
        return ResponseEntity.ok(results);
    }

    /**
     * 특정 회차의 당첨 통계 조회
     */
    @GetMapping("/statistics/{drawNo}")
    public ResponseEntity<ResultStatisticsResponse> getStatistics(@PathVariable Long drawNo) {
        log.info("Fetching statistics for draw: {}", drawNo);
        ResultStatisticsResponse statistics = resultCalculationService.getStatistics(drawNo);
        return ResponseEntity.ok(statistics);
    }
}
