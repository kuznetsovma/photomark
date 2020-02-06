package ru.codeforensics.photomark.uploadapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"ru.codeforensics.photomark"})
@EnableJpaRepositories(basePackages = {"ru.codeforensics.photomark.model.repo"})
@EntityScan(basePackages = {"ru.codeforensics.photomark.model.entities"})
@PropertySource({"classpath:/kafka.properties", "classpath:/db.properties"})
@EnableScheduling
public class UploadAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(UploadAppApplication.class, args);
  }

}
