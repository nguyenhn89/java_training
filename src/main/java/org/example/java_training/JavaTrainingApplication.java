package org.example.java_training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "org.example.java_training.repository")
public class JavaTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaTrainingApplication.class, args);
    }

}
