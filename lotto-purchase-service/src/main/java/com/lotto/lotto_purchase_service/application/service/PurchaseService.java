package com.lotto.lotto_purchase_service.application.service;

import com.lotto.lotto_api.purchase.dto.PurchaseListResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import com.lotto.lotto_purchase_service.application.LottoNumberGenerator;
import com.lotto.lotto_purchase_service.client.DrawServiceClient;
import com.lotto.lotto_purchase_service.domain.entity.LottoNumbers;
import com.lotto.lotto_purchase_service.domain.entity.Purchase;
import com.lotto.lotto_purchase_service.domain.repository.PurchaseRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final DrawServiceClient drawServiceClient;

    @Transactional
    public PurchaseResponse purchase() {
        Long currentDrawNo = drawServiceClient.getCurrentDrawNo();
        return executePurchase(currentDrawNo);
    }

    @Transactional
    public PurchaseResponse purchaseWithFaultTolerance(int delay, int faultPercent) {
        Long currentDrawNo = drawServiceClient.getCurrentDrawNoWithFaultTolerance(delay, faultPercent);
        return executePurchase(currentDrawNo);
    }

    public PurchaseListResponse getPurchasesByDrawNo(Long drawNo) {
        List<Purchase> purchases = purchaseRepository.findByDrawNo(drawNo);
        return createPurchaseListResponse(purchases);
    }

    public PurchaseListResponse getAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAllByOrderByPurchasedAtDesc();
        return createPurchaseListResponse(purchases);
    }

    private PurchaseResponse executePurchase(Long drawNo) {
        LottoNumbers lottoNumbers = generateLottoNumbers();
        Purchase purchase = createAndSavePurchase(drawNo, lottoNumbers);
        return mapToPurchaseResponse(purchase);
    }

    private LottoNumbers generateLottoNumbers() {
        List<Integer> generatedNumbers = LottoNumberGenerator.generate();
        return new LottoNumbers(generatedNumbers);
    }

    private Purchase createAndSavePurchase(Long drawNo, LottoNumbers lottoNumbers) {
        Purchase purchase = Purchase.of(drawNo, lottoNumbers);
        return purchaseRepository.save(purchase);
    }

    private PurchaseListResponse createPurchaseListResponse(List<Purchase> purchases) {
        return PurchaseListResponse.builder()
                .totalCount(purchases.size())
                .purchases(mapToPurchaseResponses(purchases))
                .build();
    }

    private List<PurchaseResponse> mapToPurchaseResponses(List<Purchase> purchases) {
        return purchases.stream()
                .map(this::mapToPurchaseResponse)
                .collect(Collectors.toList());
    }

    private PurchaseResponse mapToPurchaseResponse(Purchase purchase) {
        return PurchaseResponse.builder()
                .id(purchase.getId())
                .drawNo(purchase.getDrawNo())
                .numbers(purchase.getLottoNumbers().toList())
                .purchasedAt(purchase.getPurchasedAt())
                .build();
    }
}
