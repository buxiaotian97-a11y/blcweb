package com.example.blcweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.blcweb.repository")
@EntityScan(basePackages = "com.example.blcweb.entity")
public class BlcwebApplication {
  public static void main(String[] args) {
    SpringApplication.run(BlcwebApplication.class, args);
  }
}
