package com.capgemini.bedwards.almon.almonframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.capgemini.bedwards.almon")
public class AlmonFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlmonFrameworkApplication.class, args);
    }

}
