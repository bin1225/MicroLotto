package com.lotto.lotto_purchase_service.controller;

import com.lotto.lotto_api.purchase.dto.PurchaseListResponse;
import com.lotto.lotto_api.purchase.dto.PurchaseResponse;
import com.lotto.lotto_purchase_service.application.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> purchase() {
        PurchaseResponse response = purchaseService.purchase();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/draw/{drawNo}")
    public ResponseEntity<PurchaseListResponse> getPurchasesByDraw(
            @PathVariable Long drawNo) {
        PurchaseListResponse response = purchaseService.getPurchasesByDrawNo(drawNo);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PurchaseListResponse> getAllPurchases() {
        PurchaseListResponse response = purchaseService.getAllPurchases();
        return ResponseEntity.ok(response);
    }
}