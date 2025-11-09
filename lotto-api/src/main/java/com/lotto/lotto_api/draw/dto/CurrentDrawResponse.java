package com.lotto.lotto_api.draw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentDrawResponse {

    private Long drawNo;

    private String startDate;

    private String endDate;

    private Boolean isClosed;
}