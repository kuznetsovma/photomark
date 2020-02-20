package ru.codeforensics.photomark.parserapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication(scanBasePackages = {"ru.codeforensics.photomark"})
@EnableCassandraRepositories
@PropertySource({"classpath:/cassandra.properties", "classpath:/kafka.properties",
    "classpath:/ceph.properties"})
public class ParserAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParserAppApplication.class, args);
  }

}
