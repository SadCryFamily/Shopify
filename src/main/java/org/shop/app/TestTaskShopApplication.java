package org.shop.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestTaskShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestTaskShopApplication.class, args);
	}

}
