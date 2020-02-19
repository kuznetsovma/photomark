package ru.codeforensics.photomark.transfer;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PhotoCollationListTransfer {

  private List<PhotoCollationTransfer> collations = new ArrayList<>();
  private Long total;

}
