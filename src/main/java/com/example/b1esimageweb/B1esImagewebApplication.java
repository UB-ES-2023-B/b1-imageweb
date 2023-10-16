package com.example.b1esimageweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.b1esimageweb.web.Security.SecretKeyGenerator;


@SpringBootApplication
public class B1esImagewebApplication {

    public static void main(String[] args) {
        new SecretKeyGenerator();
        SpringApplication.run(B1esImagewebApplication.class, args);
    }

}
