package ru.codeforensics.photomark.model.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.Photo;

public interface PhotoRepo extends CrudRepository<Photo, Long> {

  Optional<Photo> findByCode(String code);
}
