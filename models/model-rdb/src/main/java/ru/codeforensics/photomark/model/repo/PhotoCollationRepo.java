package ru.codeforensics.photomark.model.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.codeforensics.photomark.model.entities.PhotoCollation;

import java.util.List;

public interface PhotoCollationRepo extends PagingAndSortingRepository<PhotoCollation, Long> {

  @Override
  List<PhotoCollation> findAll();

  Page<PhotoCollation> findAll(Pageable pageable);

  @Override
  <S extends PhotoCollation> S save(S s);

}
