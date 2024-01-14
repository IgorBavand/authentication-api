package com.igorbavand.authenticationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AuthenticationapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationapiApplication.class, args);
    }
}
