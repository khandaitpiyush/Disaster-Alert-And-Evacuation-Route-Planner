package com.example.disaster_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController; // Assuming your endpoints were here, as per the brief

@SpringBootApplication
// The brief mentioned endpoints were in the main class, so @RestController is needed here
@RestController
public class DisasterManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DisasterManagementApplication.class, args);
    }

    // NOTE: Your core endpoints (/api/events, /api/stream/events) and database
    // seeding logic should be defined inside this class or moved to a dedicated
    // controller and initializer component.
}