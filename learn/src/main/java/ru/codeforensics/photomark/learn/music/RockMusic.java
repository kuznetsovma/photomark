package ru.codeforensics.photomark.learn.music;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RockMusic implements Music {

  @Override
  public String getSong() {
    return "SexPistols - My Way - " + LocalDateTime.now();
  }
}
