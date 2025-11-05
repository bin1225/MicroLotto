package com.lotto.lotto_result_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.lotto.lotto_api",
		"com.lotto.lotto_result_service",
		"com.lotto.util"
})
public class LottoResultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottoResultServiceApplication.class, args);
	}

}
