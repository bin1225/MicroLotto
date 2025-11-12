package com.lotto.lotto_result_service.client;

import com.lotto.lotto_api.draw.dto.WinningNumberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrawServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DrawServiceClient drawServiceClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(drawServiceClient, "drawServiceUrl", "http://localhost:7002");
    }

    @Test
    @DisplayName("draw-service로부터 당첨번호를 조회한다")
    void getWinningNumber() {
        // given
        Long drawNo = 1L;
        WinningNumberResponse mockResponse = WinningNumberResponse.builder()
                .drawNo(1L)
                .winningNumbers(List.of(1, 2, 3, 4, 5, 6))
                .bonusNumber(7)
                .build();

        when(restTemplate.getForObject(anyString(), eq(WinningNumberResponse.class)))
                .thenReturn(mockResponse);

        // when
        WinningNumberResponse result = drawServiceClient.getWinningNumber(drawNo);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDrawNo()).isEqualTo(1L);
        assertThat(result.getWinningNumbers()).containsExactly(1, 2, 3, 4, 5, 6);
        assertThat(result.getBonusNumber()).isEqualTo(7);
    }
}
