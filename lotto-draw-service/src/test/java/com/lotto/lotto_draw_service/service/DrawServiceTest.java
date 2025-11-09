package com.lotto.lotto_draw_service.service;

import static org.mockito.Mockito.when;

import com.lotto.lotto_api.draw.dto.CurrentDrawResponse;
import com.lotto.lotto_draw_service.domain.entity.Draw;
import com.lotto.lotto_draw_service.domain.repository.DrawRepository;
import com.lotto.util.error.ErrorMessage;
import java.time.LocalDate;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DrawServiceTest {

    @Mock
    private DrawRepository drawRepository;

    @InjectMocks
    private DrawService drawService;


    @Test
    @DisplayName("현재 날짜가 포함된 회차가 존재하면 DTO로 변환해 반환한다")
    void getCurrentDraw_success() {
        // given
        LocalDate today = LocalDate.now();
        Draw draw = Draw.builder()
                .id(1L)
                .drawNo(1L)
                .startDate(today.minusDays(1))
                .endDate(today.plusDays(1))
                .isClosed(false)
                .build();

        when(drawRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today))
                .thenReturn(Optional.of(draw));

        // when
        CurrentDrawResponse response = drawService.getCurrentDraw();

        //then
        Assertions.assertThat(response.getDrawNo()).isEqualTo(draw.getDrawNo());
    }

    @Test
    @DisplayName("현재 날짜가 포함된 회차가 없으면 예외를 던진다")
    void getCurrentDraw_notFound() {
        //given
        LocalDate today = LocalDate.now();
        when(drawRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today))
                .thenReturn(Optional.empty());

        //when and then
        Assertions.assertThatThrownBy(() -> drawService.getCurrentDraw())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(ErrorMessage.NOT_EXIST_CURRENT_DRAW.getMessage());
    }
}