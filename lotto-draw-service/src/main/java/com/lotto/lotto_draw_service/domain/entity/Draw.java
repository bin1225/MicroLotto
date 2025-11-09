package com.lotto.lotto_draw_service.domain.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Draw {

    private Long id;
    private Long drawNo;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isClosed;

    @Builder
    public Draw(Long id, Long drawNo, LocalDate startDate, LocalDate endDate, Boolean isClosed) {
        this.id = id;
        this.drawNo = drawNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isClosed = isClosed;
    }
}