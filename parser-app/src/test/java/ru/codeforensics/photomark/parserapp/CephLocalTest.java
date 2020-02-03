package ru.codeforensics.photomark.parserapp;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.StringUtils;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CephLocalTest {

  @Test
  public void connect() {
    String accessKey = "GQAETM0ZWOEPQ40RQP38";
    String secretKey = "FPFNluBqrW8SC9on2vgHepP3tVtf09MgQbHLi4RL";
    String endpoint = "http://192.168.1.37:8000";

    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    AmazonS3 conn = new AmazonS3Client(credentials);
    conn.setEndpoint(endpoint);
    /*
    1.11
    AmazonS3 conn = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .setEndpointConfiguration(new EndpointConfiguration())
        .build();
    */



    List<Bucket> buckets = conn.listBuckets();
    for (Bucket bucket : buckets) {
      System.out.println(bucket.getName() + "\t" +
          StringUtils.fromDate(bucket.getCreationDate()));
    }
  }
}
