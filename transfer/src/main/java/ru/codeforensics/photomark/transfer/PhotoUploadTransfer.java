package ru.codeforensics.photomark.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class PhotoUploadTransfer {

  @JsonProperty(required = true)
  private UUID messageId;
  @JsonProperty(required = true)
  private UUID cameraId;
  private String code;
  @JsonProperty(value = "image", required = true)
  private String imageData;
  private Integer imageCenterX;
  private Integer imageCenterY;
  @JsonProperty("brightness")
  private Double imageBrightness;
  @JsonProperty("contrast")
  private Double imageContrast;

  @JsonProperty("coordinate")
  public void setImageCenter(Map<String, String> coordinates) {
    this.imageCenterX = Integer.parseInt(coordinates.get("x"));
    this.imageCenterY = Integer.parseInt(coordinates.get("y"));
  }

}
