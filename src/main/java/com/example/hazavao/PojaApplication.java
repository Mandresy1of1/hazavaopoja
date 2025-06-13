package com.example.hazavao;

import io.github.cdimascio.dotenv.Dotenv; // Import this

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PojaApplication {

  public static void main(String[] args) {
    // Load .env variables before Spring Boot starts
    Dotenv dotenv = Dotenv.load();
    dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
    );

    SpringApplication.run(PojaApplication.class, args);
  }
}