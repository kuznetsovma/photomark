package ru.codeforensics.photomark.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.transfer.FileWithMetaTransfer;

@Service
public class CephService {

  public static final String CLIENT_ID_KEY = "clientid";
  public static final String LINE_NAME_KEY = "linename";
  public static final String FILE_NAME_KEY = "filename";


  @Value("${system.ceph.bucketName}")
  private String bucketName;

  @Autowired
  private AmazonS3 cephConnection;

  @PostConstruct
  private void init() {
    if (!cephConnection.doesBucketExist(bucketName)) {
      cephConnection.createBucket(bucketName);
    }
  }

  public void upload(FileWithMetaTransfer fileWithMetaTransfer) {
    ByteArrayInputStream input = new ByteArrayInputStream(fileWithMetaTransfer.getFileData());

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.addUserMetadata(CLIENT_ID_KEY, "" + fileWithMetaTransfer.getClientId());
    metadata.addUserMetadata(LINE_NAME_KEY, fileWithMetaTransfer.getLineName());
    metadata.addUserMetadata(FILE_NAME_KEY, fileWithMetaTransfer.getFileName());

    cephConnection.putObject(bucketName, fileWithMetaTransfer.getCode(), input, metadata);
  }
}
