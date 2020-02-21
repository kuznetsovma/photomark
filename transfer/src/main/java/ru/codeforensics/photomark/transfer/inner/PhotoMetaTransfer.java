package ru.codeforensics.photomark.transfer.inner;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoMetaTransfer {

  private String code;
  private Long clientId;
  private String lineName;
  private String ext;
  private LocalDateTime uploaded;
}
