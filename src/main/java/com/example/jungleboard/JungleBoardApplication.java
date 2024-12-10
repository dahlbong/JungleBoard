package com.example.jungleboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:.env")
public class JungleBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(JungleBoardApplication.class, args);
    }

}
