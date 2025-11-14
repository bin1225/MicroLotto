package com.lotto.lotto_purchase_service.application.service;

import com.lotto.lotto_api.purchase.dto.PurchaseListResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import com.lotto.lotto_purchase_service.application.LottoNumberGenerator;
import com.lotto.lotto_purchase_service.client.DrawServiceClient;
import com.lotto.lotto_purchase_service.domain.entity.LottoNumbers;
import com.lotto.lotto_purchase_service.domain.entity.Purchase;
import com.lotto.lotto_purchase_service.domain.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
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

        return getPurchaseResponse(saved);
    }

    @Transactional
    public PurchaseResponse purchase(int delay, int faultPercent) {
        // 1. 현재 진행 중인 회차 조회
        Long currentDrawNo = drawServiceClient.getCurrentDrawNo(delay, faultPercent);

        // 2. 로또 번호 자동 생성
        List<Integer> generatedNumbers = LottoNumberGenerator.generate();
        LottoNumbers lottoNumbers = new LottoNumbers(generatedNumbers);

        // 3. 구매 정보 저장
        Purchase purchase = Purchase.builder()
                .drawNo(currentDrawNo)
                .lottoNumbers(lottoNumbers)
                .build();

        Purchase saved = purchaseRepository.save(purchase);

        return getPurchaseResponse(saved);
    }

    /**
     * 특정 회차별 구매 내역 조회
     */
    public PurchaseListResponse getPurchasesByDrawNo(Long drawNo) {
        List<Purchase> purchases = purchaseRepository.findByDrawNo(drawNo);
        return getPurchaseListResponse(purchases);
    }

    /**
     * 전체 구매 내역 조회
     */
    public PurchaseListResponse getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAllByOrderByPurchasedAtDesc();
        return getPurchaseListResponse(purchases);
    }

    private static PurchaseListResponse getPurchaseListResponse(List<Purchase> purchases) {
        return PurchaseListResponse.builder()
                .totalCount(purchases.size())
                .purchases(purchases.stream()
                        .map(PurchaseService::getPurchaseResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private static PurchaseResponse getPurchaseResponse(Purchase saved) {
        return PurchaseResponse.builder()
                .id(saved.getId())
                .drawNo(saved.getDrawNo())
                .numbers(saved.getLottoNumbers().getNumberList())
                .purchasedAt(saved.getPurchasedAt())
                .build();
    }
}