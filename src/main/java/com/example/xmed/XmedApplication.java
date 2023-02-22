package com.example.xmed;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAdminServer
public class XmedApplication {
    public static void main(String[] args) {
        SpringApplication.run(XmedApplication.class, args);

    }
}
