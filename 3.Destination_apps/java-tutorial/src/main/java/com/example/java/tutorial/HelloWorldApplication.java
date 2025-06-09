package com.example.java.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.sap.cloud.sdk", "com.example.java.tutorial"})
@ServletComponentScan({"com.sap.cloud.sdk", "com.example.java.tutorial"})
@SpringBootApplication
public class HelloWorldApplication {

    public static void main(String[] args) {

        SpringApplication.run(HelloWorldApplication.class, args);
        System.out.println("Hello World!");
    }

}
