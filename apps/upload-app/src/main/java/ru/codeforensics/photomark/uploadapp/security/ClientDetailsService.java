package ru.codeforensics.photomark.uploadapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.model.repo.ClientRepo;

import java.util.Optional;

@Service
public class ClientDetailsService implements UserDetailsService {

  @Autowired
  private ClientRepo clientRepo;

  @Override
  public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
    Optional<Client> client = clientRepo.findByKey(apiKey);

    if (StringUtils.isEmpty(apiKey)) {
      throw new UsernameNotFoundException("API key is empty");
    }
    if (!client.isPresent()) {
      throw new UsernameNotFoundException("API key not found");
    }

    return new ClientDetails(client.get());
  }

}
