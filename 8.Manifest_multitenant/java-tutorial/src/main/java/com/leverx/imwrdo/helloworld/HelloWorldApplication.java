package com.leverx.imwrdo.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.sap.cloud.sdk", "com.leverx.imwrdo.helloworld"})
@ServletComponentScan({"com.sap.cloud.sdk", "com.leverx.imwrdo.helloworld"})
@SpringBootApplication
public class HelloWorldApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldApplication.class, args);
    }

}
