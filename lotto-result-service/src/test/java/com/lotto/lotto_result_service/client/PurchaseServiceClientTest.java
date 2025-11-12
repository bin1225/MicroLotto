package com.lotto.lotto_result_service.client;

import com.lotto.lotto_api.purchase.dto.PurchaseListResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PurchaseServiceClient purchaseServiceClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(purchaseServiceClient, "purchaseServiceUrl", "http://localhost:7001");
    }

    @Test
    @DisplayName("purchase-service로부터 회차별 구매 내역을 조회한다")
    void getPurchasesByDrawNo() {
        // given
        Long drawNo = 1L;
        PurchaseListResponse mockResponse = PurchaseListResponse.builder()
                .totalCount(2)
                .purchases(List.of(
                        PurchaseResponse.builder()
                                .id(1L)
                                .drawNo(1L)
                                .numbers(List.of(1, 2, 3, 4, 5, 6))
                                .purchasedAt(LocalDateTime.now())
                                .build(),
                        PurchaseResponse.builder()
                                .id(2L)
                                .drawNo(1L)
                                .numbers(List.of(7, 8, 9, 10, 11, 12))
                                .purchasedAt(LocalDateTime.now())
                                .build()
                ))
                .build();

        when(restTemplate.getForObject(anyString(), eq(PurchaseListResponse.class)))
                .thenReturn(mockResponse);

        // when
        List<PurchaseResponse> results = purchaseServiceClient.getPurchasesByDrawNo(drawNo);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getNumbers()).containsExactly(1, 2, 3, 4, 5, 6);
        assertThat(results.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("구매 내역이 없으면 빈 리스트를 반환한다")
    void getPurchasesByDrawNo_EmptyResult() {
        // given
        Long drawNo = 999L;
        when(restTemplate.getForObject(anyString(), eq(PurchaseListResponse.class)))
                .thenReturn(null);

        // when
        List<PurchaseResponse> results = purchaseServiceClient.getPurchasesByDrawNo(drawNo);

        // then
        assertThat(results).isEmpty();
    }
}
