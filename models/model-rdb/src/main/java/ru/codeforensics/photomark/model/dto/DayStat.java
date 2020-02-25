package ru.codeforensics.photomark.model.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DayStat {

  private LocalDate date;
  private Long value;
}
