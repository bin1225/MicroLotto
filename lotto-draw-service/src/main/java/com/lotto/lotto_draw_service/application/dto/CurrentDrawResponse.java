package com.lotto.lotto_draw_service.application.dto;

import com.lotto.lotto_draw_service.domain.entity.Draw;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentDrawResponse {

    private Long drawNo;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isClosed;

    public static CurrentDrawResponse from(Draw draw) {
        return CurrentDrawResponse.builder()
                .drawNo(draw.getDrawNo())
                .startDate(draw.getStartDate())
                .endDate(draw.getEndDate())
                .isClosed(draw.getIsClosed())
                .build();
    }
}
