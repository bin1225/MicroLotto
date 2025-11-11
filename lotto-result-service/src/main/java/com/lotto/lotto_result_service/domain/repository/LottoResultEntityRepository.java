package com.lotto.lotto_result_service.domain.repository;

import com.lotto.lotto_result_service.domain.entity.LottoResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LottoResultEntityRepository extends JpaRepository<LottoResultEntity, Long> {

    List<LottoResultEntity> findByDrawNo(Long drawNo);

    @Query("SELECT COUNT(r) FROM LottoResultEntity r WHERE r.drawNo = :drawNo AND r.rankValue = :rank")
    Long countByDrawNoAndRank(@Param("drawNo") Long drawNo, @Param("rank") Integer rank);

    @Query("SELECT SUM(r.prizeAmount) FROM LottoResultEntity r WHERE r.drawNo = :drawNo AND r.rankValue = :rank")
    Long sumPrizeAmountByDrawNoAndRank(@Param("drawNo") Long drawNo, @Param("rank") Integer rank);

    @Query("SELECT SUM(r.prizeAmount) FROM LottoResultEntity r WHERE r.drawNo = :drawNo")
    Long sumTotalPrizeAmountByDrawNo(@Param("drawNo") Long drawNo);
}
