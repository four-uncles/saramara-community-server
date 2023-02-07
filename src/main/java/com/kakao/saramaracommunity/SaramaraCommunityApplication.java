package com.kakao.saramaracommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SaramaraCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaramaraCommunityApplication.class, args);
	}

}
