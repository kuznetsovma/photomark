package ru.codeforensics.photomark.model.repo;

import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.entities.PhotoCollation;

import java.util.List;

public interface PhotoCollationRepo extends CrudRepository<PhotoCollation, Long> {

  @Override
  List<PhotoCollation> findAll();

  @Override
  <S extends PhotoCollation> S save(S s);

}
