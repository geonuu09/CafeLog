package com.project.cafelogproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CafeLogProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeLogProjectApplication.class, args);
    }

}
