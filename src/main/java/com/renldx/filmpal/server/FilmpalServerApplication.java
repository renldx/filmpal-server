package com.renldx.filmpal.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FilmpalServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmpalServerApplication.class, args);
    }

}
