package com.example.cosmetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.example.cosmetest.data.repository")
@SpringBootApplication
public class CosmetestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CosmetestApplication.class, args);
    }
}

