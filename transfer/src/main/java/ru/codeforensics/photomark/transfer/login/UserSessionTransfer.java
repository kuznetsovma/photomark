package ru.codeforensics.photomark.transfer.login;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserSessionTransfer {

  private String token;
  private LocalDateTime created;
  private LocalDateTime expired;
}
