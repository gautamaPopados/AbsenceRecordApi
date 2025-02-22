package com.gautama.abscencerecordhitsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.gautama.abscencerecordhitsbackend.core.repository")
public class AbscenceRecordHitsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbscenceRecordHitsBackendApplication.class, args);
    }

}
