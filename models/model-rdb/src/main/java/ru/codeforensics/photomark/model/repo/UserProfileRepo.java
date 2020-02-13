package ru.codeforensics.photomark.model.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.UserProfile;

public interface UserProfileRepo extends CrudRepository<UserProfile, Long> {

  Optional<UserProfile> findByEmailAndPasswordHash(String email, String hash);
}
