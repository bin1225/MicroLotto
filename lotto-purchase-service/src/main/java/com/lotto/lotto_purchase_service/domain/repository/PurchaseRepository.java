package com.lotto.lotto_purchase_service.domain.repository;

import com.lotto.lotto_purchase_service.domain.entity.Purchase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByDrawNo(Long drawNo);

    List<Purchase> findAllByOrderByPurchasedAtDesc();
}