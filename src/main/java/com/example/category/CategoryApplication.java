package com.example.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.category"})
public class CategoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CategoryApplication.class, args);
    }

}
