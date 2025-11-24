package com.lotto.lotto_purchase_service.application;

import com.lotto.lotto_purchase_service.domain.entity.LottoNumbers;
import com.lotto.util.Randoms;
import java.util.List;

public final class LottoNumberGenerator {

    private LottoNumberGenerator() {
        throw new AssertionError("인스턴스 생성 불가");
    }

    public static List<Integer> generate() {
        return Randoms.pickUniqueNumbersInRange(
                LottoNumbers.getMinNumber(),
                LottoNumbers.getMaxNumber(),
                LottoNumbers.getCount()
        );
    }
}
