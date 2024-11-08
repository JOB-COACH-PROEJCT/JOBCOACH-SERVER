package org.v1.job_coach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobCoachApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobCoachApplication.class, args);
    }

}
