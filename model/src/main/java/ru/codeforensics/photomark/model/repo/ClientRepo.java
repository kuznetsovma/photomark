package ru.codeforensics.photomark.model.repo;

import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.Client;

public interface ClientRepo extends CrudRepository<Client, Long> {

  @Cacheable
  @Override
  Optional<Client> findById(Long id);
}
