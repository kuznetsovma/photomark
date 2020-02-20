package ru.codeforensics.photomark.model.cassandra;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@Table
public class PhotoMeta {

  @PrimaryKey
  private String code;
  private Long clientId;
  private String lineName;
  private String ext;
}
