package com.lotto.lotto_draw_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {
		"com.lotto.lotto_api",
		"com.lotto.lotto_draw_service",
		"com.lotto.util"
})
public class LottoDrawServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottoDrawServiceApplication.class, args);
	}

}
