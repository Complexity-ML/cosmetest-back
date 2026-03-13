package com.example.cosmetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableJpaRepositories(basePackages = "com.example.cosmetest.data.repository")
@EnableScheduling
@SpringBootApplication
public class CosmetestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CosmetestApplication.class, args);
    }
}

