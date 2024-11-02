package com.agorohov.restwallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RestWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestWalletApplication.class, args);
    }

}
