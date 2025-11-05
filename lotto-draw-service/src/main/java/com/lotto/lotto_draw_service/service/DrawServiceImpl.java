package com.lotto.lotto_draw_service.service;

import com.lotto.lotto_api.draw.DrawService;
import com.lotto.lotto_api.draw.DrawTestDto;
import com.lotto.util.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class DrawServiceImpl implements DrawService {

    private final ServiceUtil serviceUtil;

    public DrawServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public DrawTestDto test() {
        log.info("Draw test called");
        return new DrawTestDto(true);
    }
}
