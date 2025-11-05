package com.lotto.lotto_purchase_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.lotto.lotto_api",
		"com.lotto.lotto_purchase_service",
		"com.lotto.util"
})
public class LottoPurchaseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottoPurchaseServiceApplication.class, args);
	}

}
