package com.lotto.lotto_api.draw;

import org.springframework.web.bind.annotation.GetMapping;

public interface DrawService {

    @GetMapping(value = "/draw/test")
    DrawTestDto test();
}
