package com.lotto.lotto_result_service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "lotto_results")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LottoResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drawNo;

    @Column(nullable = false)
    private Long purchaseId;

    @Column(nullable = false, columnDefinition = "JSON")
    private String purchasedNumbers;

    @Column(nullable = false)
    private Integer matchCount;

    @Column(nullable = false)
    private Boolean bonusMatch;

    @Column(nullable = false)
    private Integer rankValue; // 1~5등, 0은 꽝

    @Column(nullable = false)
    private Long prizeAmount;

    public List<Integer> getPurchasedNumberList() {
        return List.of(purchasedNumbers.replace("[", "").replace("]", "")
                .split(",")).stream()
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }

    public void setPurchasedNumbers(List<Integer> numbers) {
        this.purchasedNumbers = numbers.toString();
    }
}
