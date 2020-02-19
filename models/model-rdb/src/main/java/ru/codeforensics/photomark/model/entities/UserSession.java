package ru.codeforensics.photomark.model.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import ru.codeforensics.photomark.transfer.login.UserSessionTransfer;

@Data
@Entity
public class UserSession extends AbstractEntity {

  @ManyToOne
  private UserProfile userProfile;

  private String token;

  private LocalDateTime created = LocalDateTime.now();

  private LocalDateTime expired;

  public UserSessionTransfer toTransfer() {
    return new UserSessionTransfer(token, created, expired);
  }
}
