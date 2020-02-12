package ru.codeforensics.photomark.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import ru.codeforensics.photomark.transfer.ClientTransfer;

@Data
@Entity
public class Client extends AbstractEntity {

  private String name;

  @Column(unique = true)
  private String key;

  public ClientTransfer toTransfer() {
    ClientTransfer transfer = new ClientTransfer();
    transfer.setId(id);
    transfer.setName(name);
    transfer.setKey(key);
    return transfer;
  }
}
