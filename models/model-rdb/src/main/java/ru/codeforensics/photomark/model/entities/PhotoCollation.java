package ru.codeforensics.photomark.model.entities;

import lombok.Data;
import ru.codeforensics.photomark.transfer.PhotoCollationTransfer;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
@Entity
public class PhotoCollation extends AbstractEntity {

  public static final byte STATUS_PENDING = 0;
  public static final byte STATUS_PROCESSING = 1;
  public static final byte STATUS_SUCCEED = 2;
  public static final byte STATUS_FAILED = 3;
  public static final byte STATUS_STOPPED = 4;

  public static final byte RESULT_UNKNOWN = 0;
  public static final byte RESULT_SUCCEED = 1;
  public static final byte RESULT_FAILED = 2;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column
  private LocalDateTime startedAt;

  @Column
  private LocalDateTime finishedAt;

  @Column(nullable = false)
  private byte status = STATUS_PENDING;

  @Column(nullable = false)
  private byte result = RESULT_UNKNOWN;

  @Column
  private String sampleKey;

  @Column
  private String sampleCode;

  @Column
  private String originalKey;

  @Column
  private String originalCode;

  public PhotoCollationTransfer toTransfer() {
    PhotoCollationTransfer transfer = new PhotoCollationTransfer();
    transfer.setId(id);
    transfer.setCreatedAt(createdAt);
    transfer.setStartedAt(startedAt);
    transfer.setFinishedAt(finishedAt);
    transfer.setStatus(status);
    transfer.setResult(result);
    return transfer;
  }

}
