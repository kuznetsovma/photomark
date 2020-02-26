package ru.codeforensics.photomark.transfer.inner;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhotoTransfer {

  private Long clientId;
  private LocalDateTime uploadedAt;
  private UUID messageId;
  private UUID cameraId;
  private String code;
  private String imageData;
  private Integer imageCenterX;
  private Integer imageCenterY;
  private Double imageBrightness;
  private Double imageContrast;

}
