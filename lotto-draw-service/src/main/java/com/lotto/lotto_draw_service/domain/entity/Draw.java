package com.lotto.lotto_draw_service.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDate;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Draw {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drawNo;

    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    @Column(nullable = false, updatable = false)
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