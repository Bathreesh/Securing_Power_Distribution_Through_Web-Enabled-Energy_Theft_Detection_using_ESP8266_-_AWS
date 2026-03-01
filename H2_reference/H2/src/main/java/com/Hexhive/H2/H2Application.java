package com.Hexhive.H2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Main Entry Point of the Spring Boot Application.
 * 
 * @SpringBootApplication: This is a "magic" annotation that tells Spring Boot
 *                         to:
 *                         1. Auto-configuration: Automatically configure the
 *                         app based on dependencies (like H2 database).
 *                         2. Component Scan: Look for controllers, services,
 *                         and repositories in this package.
 */
@SpringBootApplication
public class H2Application {

    /**
     * The main method is where the Java Virtual Machine (JVM) starts execution.
     */
    public static void main(String[] args) {
        // This line tells Spring to launch the entire framework and start the web
        // server.
        SpringApplication.run(H2Application.class, args);
    }
}