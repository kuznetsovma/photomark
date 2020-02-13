package ru.codeforensics.photomark.restapp.security;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.entities.UserSession;

@AllArgsConstructor
public class UserSessionDetails implements UserDetails {

  @Getter
  private UserSession userSession;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    UserProfile userProfile = userSession.getUserProfile();
    return userProfile.getAuthorities().stream()
        .map((ua) -> new SimpleGrantedAuthority(ua.name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    UserProfile userProfile = userSession.getUserProfile();
    return userProfile.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return LocalDateTime.now().isBefore(userSession.getExpired());
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
