package ru.codeforensics.photomark.transfer;

import lombok.Data;

@Data
public class PhotoCollationTaskTransfer {

  private Long photoCollationId;
  private byte[] sampleFileData;
  private String sampleFileName;

}
