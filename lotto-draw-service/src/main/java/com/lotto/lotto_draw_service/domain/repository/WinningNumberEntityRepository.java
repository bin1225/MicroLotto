package com.lotto.lotto_draw_service.domain.repository;

import com.lotto.lotto_draw_service.domain.entity.WinningNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinningNumberEntityRepository extends JpaRepository<WinningNumberEntity, Long> {
}
