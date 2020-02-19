package ru.codeforensics.photomark.transfer;

import java.time.LocalDateTime;
import lombok.Data;
import ru.codeforensics.photomark.transfer.enums.PhotoCollationStatus;

@Data
public class PhotoCollationTransfer {

  private Long id;
  private Long userId;

  private LocalDateTime created;
  private LocalDateTime started;
  private LocalDateTime finished;

  private String code;
  private String standard;
  private String file;

  private PhotoCollationStatus status;

}
