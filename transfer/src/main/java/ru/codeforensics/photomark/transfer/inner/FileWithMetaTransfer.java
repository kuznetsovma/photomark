package ru.codeforensics.photomark.transfer.inner;

import java.io.Serializable;
import lombok.Data;

@Data
public class FileWithMetaTransfer implements Serializable {

  private Long clientId;
  private String lineName;
  private String code;
  private String fileName;
  private byte[] fileData;
}
