package ru.codeforensics.photomark.learnAddBeans.music;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class RockMusic implements Music {

  @Override
  public String getSong() {
    return "SexPistols - My Way - " + LocalDateTime.now();
  }
}
