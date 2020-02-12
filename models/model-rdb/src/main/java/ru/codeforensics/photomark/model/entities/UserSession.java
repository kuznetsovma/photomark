package ru.codeforensics.photomark.model.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class UserSession extends AbstractEntity {

  @ManyToOne
  private UserProfile userProfile;

  private String token;

  private LocalDateTime created;

  private LocalDateTime expired;
}
