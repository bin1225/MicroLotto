package com.lotto.lotto_purchase_service.application;

import com.lotto.lotto_purchase_service.domain.entity.LottoNumbers;
import com.lotto.util.Randoms;
import java.util.List;

public class LottoNumberGenerator {

    public static List<Integer> generate() {
        return Randoms.pickUniqueNumbersInRange(LottoNumbers.MIN_NUMBER, LottoNumbers.MAX_NUMBER,
                LottoNumbers.LOTTO_NUMBER_COUNT);
    }
}
