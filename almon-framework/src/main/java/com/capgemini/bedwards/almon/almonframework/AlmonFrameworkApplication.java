package com.capgemini.bedwards.almon.almonframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.capgemini.bedwards.almon")
@EnableJpaRepositories(basePackages = "com.capgemini.bedwards.almon")
@EntityScan(basePackages = "com.capgemini.bedwards.almon")
public class AlmonFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlmonFrameworkApplication.class, args);
    }

}
