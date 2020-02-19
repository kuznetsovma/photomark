package ru.codeforensics.photomark.transfer.inner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoCollationNNTransfer {

  private String code;
  private byte[] sampleContent;
  private byte[] standardContent;
}
