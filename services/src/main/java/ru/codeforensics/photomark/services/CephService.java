package ru.codeforensics.photomark.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.transfer.FileWithMetaTransfer;

@Service
public class CephService {

  @Value("${system.ceph.bucketName}")
  private String bucketName;

  @Autowired
  private AmazonS3 cephConnection;

  public void upload(FileWithMetaTransfer fileWithMetaTransfer) {
    ByteArrayInputStream input = new ByteArrayInputStream(fileWithMetaTransfer.getFileData());

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.addUserMetadata("clientId", "" + fileWithMetaTransfer.getClientId());
    metadata.addUserMetadata("lineName", fileWithMetaTransfer.getLineName());
    metadata.addUserMetadata("fileName", fileWithMetaTransfer.getFileName());

    cephConnection.putObject(bucketName, fileWithMetaTransfer.getCode(), input, metadata);
  }
}
