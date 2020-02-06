package ru.codeforensics.photomark.restapp;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.codeforensics.photomark.model.repo.ClientRepo;

@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties"})
class RestAppApplicationTests {

  @Autowired
  private ClientRepo clientRepo;

  @Test
  void contextLoads() {
    clientRepo.findAll();
  }

}
