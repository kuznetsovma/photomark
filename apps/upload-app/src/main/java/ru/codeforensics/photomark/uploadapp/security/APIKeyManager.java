package ru.codeforensics.photomark.uploadapp.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.model.repo.ClientRepo;

@Component
public class APIKeyManager implements AuthenticationManager {

  @Autowired
  private ClientRepo clientRepo;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String xApiKey = (String) authentication.getPrincipal();
    Optional<Client> clientOptional = clientRepo.findByKey(xApiKey);

    if (!clientOptional.isPresent()) {
      throw new BadCredentialsException("No rights");
    }

    authentication.setAuthenticated(true);
    return authentication;
  }

}
