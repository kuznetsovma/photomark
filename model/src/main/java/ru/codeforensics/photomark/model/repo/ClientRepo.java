package ru.codeforensics.photomark.model.repo;

import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.Client;

public interface ClientRepo extends CrudRepository<Client, Long> {

  String CACHE_NAME = "clientByKey";

  @Cacheable(CACHE_NAME)
  Optional<Client> findByKey(String key);

  @CachePut(cacheNames = CACHE_NAME, key = "#s.key")
  @Override
  <S extends Client> S save(S s);

  @CacheEvict(cacheNames = CACHE_NAME, key = "#client.key")
  @Override
  void delete(Client client);
}
