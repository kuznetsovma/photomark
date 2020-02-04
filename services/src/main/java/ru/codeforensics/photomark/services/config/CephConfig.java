package ru.codeforensics.photomark.services.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/ceph.properties")
public class CephConfig {

  @Value("${system.ceph.accessKey}")
  private String accessKey;
  @Value("${system.ceph.secretKey}")
  private String secretKey;
  @Value("${system.ceph.endpoint}")
  private String endpoint;

  @Bean
  public AmazonS3 cephConnection() {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 conn = new AmazonS3Client(credentials);
    conn.setEndpoint(endpoint);
    return conn;
  }
}
