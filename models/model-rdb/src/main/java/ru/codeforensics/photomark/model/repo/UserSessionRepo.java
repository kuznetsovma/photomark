package ru.codeforensics.photomark.model.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.UserSession;

public interface UserSessionRepo extends CrudRepository<UserSession, Long> {

  Optional<UserSession> findByToken(String token);
}
