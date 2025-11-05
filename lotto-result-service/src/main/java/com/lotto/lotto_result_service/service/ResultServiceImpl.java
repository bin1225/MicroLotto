package com.lotto.lotto_result_service.service;

import com.lotto.lotto_api.result.ResultService;
import com.lotto.lotto_api.result.ResultTestDto;
import com.lotto.util.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ResultServiceImpl implements ResultService {

    private final ServiceUtil serviceUtil;

    public ResultServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public ResultTestDto test() {
        log.info("Result test called");
        return new ResultTestDto(true);
    }
}
