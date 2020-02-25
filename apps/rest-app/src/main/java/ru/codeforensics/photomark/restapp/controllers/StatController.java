package ru.codeforensics.photomark.restapp.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.codeforensics.photomark.model.dto.DayStat;
import ru.codeforensics.photomark.model.repo.PhotoCollationRepo;
import ru.codeforensics.photomark.model.repo.StatDataRepo;
import ru.codeforensics.photomark.transfer.DatePeriodTransfer;
import ru.codeforensics.photomark.transfer.StatsTransfer;

@RestController
public class StatController extends AbstractController {

  @Autowired
  private StatDataRepo statDataRepo;

  @Autowired
  private PhotoCollationRepo photoCollationRepo;

  @PostMapping("/dynamic/codes")
  public ResponseEntity dynamicCodes(@RequestBody DatePeriodTransfer datePeriodTransfer) {
    List<DayStat> dayStats = statDataRepo
        .selectDayStat(datePeriodTransfer.getFrom(), datePeriodTransfer.getTo());

    return ResponseEntity.ok(statsTransferFrom(dayStats, statDataRepo.selectTotal()));
  }

  @PostMapping("/dynamic/collations")
  public ResponseEntity dynamicCollations(@RequestBody DatePeriodTransfer datePeriodTransfer) {
    List<DayStat> dayStats = photoCollationRepo
        .selectDayStat(datePeriodTransfer.getFrom(), datePeriodTransfer.getTo());

    return ResponseEntity.ok(statsTransferFrom(dayStats, photoCollationRepo.count()));
  }


  private StatsTransfer statsTransferFrom(List<DayStat> dayStats, Long total) {
    StatsTransfer statsTransfer = new StatsTransfer();
    statsTransfer.setTotal(total);

    for (DayStat dayStat : dayStats) {
      statsTransfer.getStats().put(dayStat.getDate(), dayStat.getValue());
    }

    return statsTransfer;
  }
}
