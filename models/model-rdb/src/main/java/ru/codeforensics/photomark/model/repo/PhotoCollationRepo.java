package ru.codeforensics.photomark.model.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.codeforensics.photomark.model.entities.PhotoCollation;
import ru.codeforensics.photomark.model.entities.UserProfile;

import java.util.List;
import java.util.Optional;

public interface PhotoCollationRepo extends PagingAndSortingRepository<PhotoCollation, Long> {

  @Override
  List<PhotoCollation> findAll();

  Page<PhotoCollation> findAll(Pageable pageable);

  Page<PhotoCollation> findAllByUserProfile(UserProfile userProfile, Pageable pageable);

  Optional<PhotoCollation> findByIdAndUserProfile(Long id, UserProfile userProfile);

  @Override
  <S extends PhotoCollation> S save(S s);

}
