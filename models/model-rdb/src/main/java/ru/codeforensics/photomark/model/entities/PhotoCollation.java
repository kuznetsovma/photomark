package ru.codeforensics.photomark.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import ru.codeforensics.photomark.transfer.PhotoCollationTransfer;
import ru.codeforensics.photomark.transfer.enums.PhotoCollationStatus;

@Data
@Entity
public class PhotoCollation extends AbstractEntity {


  @ManyToOne
  private UserProfile userProfile;

  private LocalDateTime created = LocalDateTime.now();
  private LocalDate createdDate = created.toLocalDate();
  private LocalDateTime started;
  private LocalDateTime finished;

  private PhotoCollationStatus status = PhotoCollationStatus.PROCESSING;

  private String code;

  public PhotoCollationTransfer toTransfer() {
    PhotoCollationTransfer transfer = new PhotoCollationTransfer();
    transfer.setId(id);
    transfer.setUserId(null != userProfile ? userProfile.getId() : null);
    transfer.setCreated(created);
    transfer.setStarted(started);
    transfer.setFinished(finished);
    transfer.setStatus(status);
    transfer.setCode(code);
    return transfer;
  }

}
