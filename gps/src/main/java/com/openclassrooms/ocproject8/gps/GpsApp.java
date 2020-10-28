package com.openclassrooms.ocproject8.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.openclassrooms.ocproject8.gps.GpsApp;

@SpringBootApplication
@ComponentScan({"com.openclassrooms.ocproject8.gps","com.openclassrooms.ocproject8.shared"})
public class GpsApp {

    public static void main(String[] args) {
        SpringApplication.run(GpsApp.class, args);
    }

}
