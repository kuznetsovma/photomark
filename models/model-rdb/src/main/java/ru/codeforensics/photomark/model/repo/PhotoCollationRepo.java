package ru.codeforensics.photomark.model.repo;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.codeforensics.photomark.model.dto.DayStat;
import ru.codeforensics.photomark.model.entities.PhotoCollation;

public interface PhotoCollationRepo extends PagingAndSortingRepository<PhotoCollation, Long> {

  Page<PhotoCollation> findAll(Pageable pageable);

  @Query("select new ru.codeforensics.photomark.model.dto.DayStat(pc.createdDate, count(pc.id)) "
      + "from PhotoCollation pc "
      + "where pc.createdDate between :fromDate and :toDate "
      + "group by pc.createdDate")
  List<DayStat> selectDayStat(LocalDate fromDate, LocalDate toDate);

}
