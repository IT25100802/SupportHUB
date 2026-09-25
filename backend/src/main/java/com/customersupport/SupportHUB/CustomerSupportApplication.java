package com.customersupport.SupportHUB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CustomerSupportApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerSupportApplication.class, args);
    }
}
