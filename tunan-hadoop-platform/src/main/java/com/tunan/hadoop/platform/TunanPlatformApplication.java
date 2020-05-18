package com.tunan.hadoop.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TunanPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(TunanPlatformApplication.class, args);
    }

}
