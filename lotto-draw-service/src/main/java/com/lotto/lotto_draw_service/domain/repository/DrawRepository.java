package com.lotto.lotto_draw_service.domain.repository;

import com.lotto.lotto_draw_service.domain.entity.Draw;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawRepository extends JpaRepository<Draw, Long> {

    // 오늘 날짜가 startDate~endDate 범위에 포함되는 회차 조회
    Optional<Draw> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

    Optional<Draw> findTopByOrderByDrawNoDesc();
}
