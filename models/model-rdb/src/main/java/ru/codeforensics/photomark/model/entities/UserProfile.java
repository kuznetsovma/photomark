package ru.codeforensics.photomark.model.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import lombok.Data;
import ru.codeforensics.photomark.transfer.enums.UserAuthority;

@Data
@Entity
public class UserProfile extends AbstractEntity {

  private String email;

  private String passwordHash;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_user_authority", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "authority")
  private Set<UserAuthority> authorities = new HashSet<>();
}
