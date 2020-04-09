package ru.codeforensics.photomark.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class LearnApplication {

  @Autowired
  private MusicPlayer player;

  @Scheduled(fixedDelay = 1_000)
  public void play() {
    player.playMusic();
  }

  public static void main(String[] args) {
    SpringApplication.run(LearnApplication.class, args);
  }

}
