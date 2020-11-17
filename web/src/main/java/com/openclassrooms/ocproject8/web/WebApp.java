package com.openclassrooms.ocproject8.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.openclassrooms.ocproject8.web","com.openclassrooms.ocproject8.shared"})
@EnableJpaRepositories(basePackages = {"com.openclassrooms.ocproject8.shared.repository"})
@EntityScan("com.openclassrooms.ocproject8.shared.domain")
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

}
