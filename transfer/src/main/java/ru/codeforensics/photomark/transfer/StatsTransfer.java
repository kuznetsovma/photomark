package ru.codeforensics.photomark.transfer;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;

@Data
public class StatsTransfer {

  private Map<LocalDate, Long> stats = new TreeMap<>();
  private Long total;

}
