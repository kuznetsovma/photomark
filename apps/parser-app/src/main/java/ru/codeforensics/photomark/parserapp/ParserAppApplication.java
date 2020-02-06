package ru.codeforensics.photomark.parserapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.codeforensics.photomark"})
@EnableJpaRepositories(basePackages = {"ru.codeforensics.photomark.model.repo"})
@EntityScan(basePackages = {"ru.codeforensics.photomark.model.entities"})
@PropertySource({"classpath:/db.properties", "classpath:/kafka.properties", "classpath:/ceph.properties"})
public class ParserAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParserAppApplication.class, args);
  }

}
