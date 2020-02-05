package ru.codeforensics.photomark.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class StatDataId implements Serializable {

  private Long clientId;
  private LocalDate date;
  private String line;
}
