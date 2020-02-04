package ru.codeforensics.photomark.model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Photo extends AbstractEntity {

  @ManyToOne
  private Client client;

  private String lineId;

  private String code;
}
