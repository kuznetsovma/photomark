package ru.codeforensics.photomark.model.repo;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.codeforensics.photomark.model.dto.DayStat;
import ru.codeforensics.photomark.model.entities.StatData;
import ru.codeforensics.photomark.model.entities.StatDataId;

public interface StatDataRepo extends CrudRepository<StatData, StatDataId> {

  @Query("select new ru.codeforensics.photomark.model.dto.DayStat(sd.id.date, sum(sd.codeCount)) "
      + "from StatData sd "
      + "where sd.id.date between :fromDate and :toDate "
      + "group by sd.id.date")
  List<DayStat> selectDayStat(LocalDate fromDate, LocalDate toDate);

  @Query("select sum(sd.codeCount) from StatData sd")
  Long selectTotal();
}
