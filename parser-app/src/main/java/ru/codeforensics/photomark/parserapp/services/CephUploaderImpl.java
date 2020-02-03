package ru.codeforensics.photomark.parserapp.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CephUploaderImpl implements CephUploader {

  @Value("${system.ceph.bucketName}")
  private String bucketName;

  @Autowired
  private AmazonS3 cephConnection;

  @Override
  public void upload(String key, byte[] data) {
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    cephConnection.putObject(bucketName, key, input, new ObjectMetadata());
  }
}
