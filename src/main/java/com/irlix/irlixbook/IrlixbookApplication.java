package com.irlix.irlixbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IrlixbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrlixbookApplication.class, args);
    }
}
