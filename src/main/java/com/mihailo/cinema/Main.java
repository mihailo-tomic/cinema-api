package com.mihailo.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@SpringBootApplication
public class Main extends RepositoryRestConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
