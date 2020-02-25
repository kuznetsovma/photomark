package ru.codeforensics.photomark.transfer;

import java.time.LocalDate;
import lombok.Data;

@Data
public class DatePeriodTransfer {

  private LocalDate from;
  private LocalDate to;
}
