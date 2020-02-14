package ru.codeforensics.photomark.restapp.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.codeforensics.photomark.model.entities.UserSession;
import ru.codeforensics.photomark.model.repo.UserSessionRepo;

@Service
public class UserSessionDetailsService implements UserDetailsService {

  @Value("${app.superadmin.xapikey}")
  private String xApiKeySuperAdmin;

  @Autowired
  private UserSessionRepo userSessionRepo;

  @Override
  public UserDetails loadUserByUsername(String xApiKey) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(xApiKey)) {
      throw new UsernameNotFoundException("API key is empty");
    }

    if (xApiKeySuperAdmin.equals(xApiKey)) {
      return new SuperAdminUserDetails();
    }

    Optional<UserSession> userSessionOptional = userSessionRepo.findByToken(xApiKey);
    if (!userSessionOptional.isPresent()) {
      throw new UsernameNotFoundException("API key not found");
    }

    return new UserSessionDetails(userSessionOptional.get());
  }
}
