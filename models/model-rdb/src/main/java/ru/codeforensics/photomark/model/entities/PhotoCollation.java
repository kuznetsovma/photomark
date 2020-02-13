package ru.codeforensics.photomark.model.entities;

import lombok.Data;
import ru.codeforensics.photomark.transfer.CollationTransfer;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class PhotoCollation extends AbstractEntity {

  public static final Integer STATUS_PENDING = 0;
  public static final Integer STATUS_PROCESSING = 1;
  public static final Integer STATUS_FINISHED = 2;

  @Column
  private Integer status = STATUS_PENDING;

  public CollationTransfer toTransfer() {
    CollationTransfer transfer = new CollationTransfer();
    transfer.setId(id);
    transfer.setStatus(status);
    return transfer;
  }

}
