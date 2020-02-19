package ru.codeforensics.photomark.transfer.admin;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import ru.codeforensics.photomark.transfer.enums.UserAuthority;

@Data
public class UserTransfer {

  private Long id;

  private String email;

  private String password;

  private Set<UserAuthority> authorities = new HashSet<>();
}
