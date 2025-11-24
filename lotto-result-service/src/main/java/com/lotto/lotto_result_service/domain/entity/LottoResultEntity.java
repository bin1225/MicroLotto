package com.lotto.lotto_result_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lotto_results")
@Getter
@NoArgsConstructor
public class LottoResultEntity {

    private static final String NUMBER_DELIMITER = ",";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drawNo;

    @Column(nullable = false)
    private Long purchaseId;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String purchasedNumbers;

    @Column(nullable = false)
    private Integer matchCount;

    @Column(nullable = false)
    private Boolean bonusMatch;

    @Column(nullable = false)
    private Integer rankValue;

    @Column(nullable = false)
    private Long prizeAmount;

    public List<Integer> getPurchasedNumberList() {
        return Arrays.stream(purchasedNumbers.split(NUMBER_DELIMITER))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
