package com.lotto.lotto_purchase_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drawNo;  // 회차 번호

    @Embedded
    private LottoNumbers lottoNumbers;

    @Column(nullable = false, updatable = false)
    private LocalDateTime purchasedAt;  // 구매 시간

    @Builder
    public Purchase(Long drawNo, LottoNumbers lottoNumbers) {
        this.drawNo = drawNo;
        this.lottoNumbers = lottoNumbers;
        this.purchasedAt = LocalDateTime.now();
    }
}