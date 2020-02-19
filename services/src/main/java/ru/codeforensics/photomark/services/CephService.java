package ru.codeforensics.photomark.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.transfer.inner.FileWithMetaTransfer;

@Service
public class CephService {

  public static final String CLIENT_ID_KEY = "clientid";
  public static final String LINE_NAME_KEY = "linename";
  public static final String FILE_NAME_KEY = "filename";


  @Value("${system.ceph.bucketName.photos}")
  private String photosBucketName;

  @Value("${system.ceph.bucketName.collation}")
  private String photoCollationsBucketName;

  @Autowired
  private AmazonS3 cephConnection;

  @PostConstruct
  private void init() {
    if (!cephConnection.doesBucketExist(photosBucketName)) {
      cephConnection.createBucket(photosBucketName);
    }

    if (!cephConnection.doesBucketExist(photoCollationsBucketName)) {
      cephConnection.createBucket(photoCollationsBucketName);
    }
  }

  public void upload(FileWithMetaTransfer fileWithMetaTransfer) {
    ByteArrayInputStream input = new ByteArrayInputStream(fileWithMetaTransfer.getFileData());

    ObjectMetadata metadata = new ObjectMetadata();
    if (null != fileWithMetaTransfer.getClientId()) {
      metadata.addUserMetadata(CLIENT_ID_KEY, "" + fileWithMetaTransfer.getClientId());
    }
    if (null != fileWithMetaTransfer.getLineName()) {
      metadata.addUserMetadata(LINE_NAME_KEY, fileWithMetaTransfer.getLineName());
    }
    metadata.addUserMetadata(FILE_NAME_KEY, fileWithMetaTransfer.getFileName());

    cephConnection.putObject(photosBucketName, fileWithMetaTransfer.getCode(), input, metadata);
  }
}
