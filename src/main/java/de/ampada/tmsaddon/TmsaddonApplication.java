package de.ampada.tmsaddon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class TmsaddonApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmsaddonApplication.class, args);
    }

}
