package com.openclassrooms.ocproject8.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.openclassrooms.ocproject8.web.WebApp;

@SpringBootApplication
@ComponentScan({"com.openclassrooms.ocproject8.web","com.openclassrooms.ocproject8.shared"})
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }

}
