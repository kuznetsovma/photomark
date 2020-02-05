package ru.codeforensics.photomark.model.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Version;
import lombok.Data;

@Data
@Entity
public class StatData {

  @EmbeddedId
  private StatDataId id;

  @Version
  protected int version;

  private int codeCount;

  public void addCount(int diff) {
    codeCount += diff;
  }
}
