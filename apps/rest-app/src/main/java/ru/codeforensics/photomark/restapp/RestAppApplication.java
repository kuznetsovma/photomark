package ru.codeforensics.photomark.restapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = {"ru.codeforensics.photomark"})
@EnableJpaRepositories(basePackages = {"ru.codeforensics.photomark.model.repo"})
@EntityScan(basePackages = {"ru.codeforensics.photomark.model.entities"})
@PropertySource({"classpath:/ceph.properties", "classpath:/db.properties"})
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
public class RestAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestAppApplication.class, args);
  }

}
