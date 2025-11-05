package com.lotto.lotto_api.purchase;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public interface PurchaseService {

    @GetMapping(value = "/purchase/test")
    PurchaseTestDto test();

    @PostMapping(value = "/lotto/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    Lotto purchaseLotto();
}
