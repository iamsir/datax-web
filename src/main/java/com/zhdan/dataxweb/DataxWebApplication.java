package com.zhdan.dataxweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author dongan.zhang
 **/
@SpringBootApplication
@EnableTransactionManagement
public class DataxWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataxWebApplication.class, args);
    }
}
