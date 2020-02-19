package ru.codeforensics.photomark.transfer;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCodeTransfer {

  private Long id;
  private LocalDateTime dateTime;
  private String standard;
  private String code;
  private String line;
}
