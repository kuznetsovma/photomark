package ru.codeforensics.photomark.services;

import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.transfer.inner.PhotoCollationNNTransfer;

@Service
public class PhotoCollationNNService {

  public boolean check(PhotoCollationNNTransfer photoCollationNNTransfer) {
    //todo: replace by code which connect to neural network service
    return 0 == photoCollationNNTransfer.hashCode() % 2;
  }
}
