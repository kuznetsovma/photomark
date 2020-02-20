package ru.codeforensics.photomark.transfer.inner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoMetaTransfer {

  private String code;
  private Long clientId;
  private String lineName;
  private String ext;
}
