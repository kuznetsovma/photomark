package ru.codeforensics.photomark.transfer;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class PhotoCodeListTransfer {

  private List<PhotoCodeTransfer> codes = new ArrayList<>();
  private long total;
}
