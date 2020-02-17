package ru.codeforensics.photomark.transfer;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoCollationTransfer {

  @Data
  public static class  PhotoCollationSampleTransfer {

    private String code;
    private String location;

  }

  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  private byte status;
  private byte result;
  private PhotoCollationSampleTransfer sample;

}
