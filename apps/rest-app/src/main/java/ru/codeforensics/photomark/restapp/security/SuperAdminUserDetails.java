package ru.codeforensics.photomark.restapp.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.codeforensics.photomark.transfer.enums.UserAuthority;

public class SuperAdminUserDetails implements UserDetails {

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(UserAuthority.values())
        .map((ua) -> new SimpleGrantedAuthority(ua.name()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return "SUPERADMIN";
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
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
