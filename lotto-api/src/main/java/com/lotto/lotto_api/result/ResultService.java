package com.lotto.lotto_api.result;

import org.springframework.web.bind.annotation.GetMapping;

public interface ResultService {

    @GetMapping(value = "/result/test")
    ResultTestDto test();
}
