package com.lotto.lotto_purchase_service.service;

import com.lotto.lotto_api.purchase.Lotto;
import com.lotto.lotto_api.purchase.PurchaseService;
import com.lotto.lotto_api.purchase.PurchaseTestDto;
import com.lotto.util.ServiceUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PurchaseServiceImpl implements PurchaseService {

    private final ServiceUtil serviceUtil;

    public PurchaseServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public PurchaseTestDto test() {
        log.info("Purchase test called");
        return new PurchaseTestDto(true);
    }

    @Override
    public Lotto purchaseLotto() {
        return new Lotto(List.of(1, 2, 3, 4, 5, 6));
    }
}
