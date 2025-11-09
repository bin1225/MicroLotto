package com.lotto.lotto_purchase_service.application.service;

import com.lotto.lotto_purchase_service.application.LottoNumberGenerator;
import com.lotto.lotto_purchase_service.application.dto.PurchaseListResponse;
import com.lotto.lotto_purchase_service.application.dto.PurchaseResponse;
import com.lotto.lotto_purchase_service.client.DrawServiceClient;
import com.lotto.lotto_purchase_service.domain.entity.LottoNumbers;
import com.lotto.lotto_purchase_service.domain.entity.Purchase;
import com.lotto.lotto_purchase_service.domain.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final DrawServiceClient drawServiceClient;

    @Transactional
    public PurchaseResponse purchase() {
        // 1. 현재 진행 중인 회차 조회
        Long currentDrawNo = drawServiceClient.getCurrentDrawNo();

        // 2. 로또 번호 자동 생성
        List<Integer> generatedNumbers = LottoNumberGenerator.generate();
        LottoNumbers lottoNumbers = new LottoNumbers(generatedNumbers);

        // 3. 구매 정보 저장
        Purchase purchase = Purchase.builder()
                .drawNo(currentDrawNo)
                .lottoNumbers(lottoNumbers)
                .build();

        Purchase saved = purchaseRepository.save(purchase);

        return PurchaseResponse.from(saved);
    }

    /**
     * 특정 회차별 구매 내역 조회
     */
    public PurchaseListResponse getPurchasesByDrawNo(Long drawNo) {
        List<Purchase> purchases = purchaseRepository.findByDrawNo(drawNo);
        return PurchaseListResponse.from(purchases);
    }

    /**
     * 전체 구매 내역 조회
     */
    public PurchaseListResponse getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAllByOrderByPurchasedAtDesc();
        return PurchaseListResponse.from(purchases);
    }
}