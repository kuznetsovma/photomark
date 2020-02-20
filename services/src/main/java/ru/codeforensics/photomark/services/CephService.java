package ru.codeforensics.photomark.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CephService {

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

  public void upload(String code, byte[] fileContent) {
    ByteArrayInputStream input = new ByteArrayInputStream(fileContent);
    cephConnection.putObject(photosBucketName, code, input, new ObjectMetadata());
  }
}
