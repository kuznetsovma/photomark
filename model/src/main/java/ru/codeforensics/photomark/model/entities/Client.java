package ru.codeforensics.photomark.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Client extends AbstractEntity {

  private String name;

  @Column(unique = true)
  private String key;
}
