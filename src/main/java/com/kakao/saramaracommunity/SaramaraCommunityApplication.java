package com.kakao.saramaracommunity;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SaramaraCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaramaraCommunityApplication.class, args);
	}

}
