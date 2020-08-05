package com.example.ustbdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GitlabdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitlabdemoApplication.class, args);

    }
}
