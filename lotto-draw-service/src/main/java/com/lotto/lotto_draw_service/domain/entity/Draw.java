package com.lotto.lotto_draw_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Draw {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long drawNo;

    @Column(nullable = false, updatable = false)
    private LocalDate startDate;

    @Column(nullable = false, updatable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isClosed;

    @Builder
    private Draw(Long id, Long drawNo, LocalDate startDate, LocalDate endDate, Boolean isClosed) {
        this.id = id;
        this.drawNo = drawNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isClosed = isClosed != null ? isClosed : false;
    }

    public static Draw createNew(Long drawNo, LocalDate startDate, LocalDate endDate) {
        return Draw.builder().drawNo(drawNo).startDate(startDate).endDate(endDate).isClosed(false).build();
    }

    public void close() {
        this.isClosed = true;
    }

    public boolean isOpen() {
        return !isClosed;
    }
}
