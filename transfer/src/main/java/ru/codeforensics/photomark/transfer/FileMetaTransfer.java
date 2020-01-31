package ru.codeforensics.photomark.transfer;

import java.io.Serializable;
import lombok.Data;

@Data
public class FileMetaTransfer implements Serializable {

  private Long clientId;
  private String lineName;
  private String code;
  private byte[] fileData;
}
