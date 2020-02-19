package ru.codeforensics.photomark.restapp.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController extends AbstractController {

  @Autowired
  private AmazonS3 cephConnection;

  @Value("${system.ceph.bucketName.photos}")
  private String photosBucketName;

  @Value("${system.ceph.bucketName.collation}")
  private String photoCollationsBucketName;


  @GetMapping("/readFileParams/{code}")
  public ResponseEntity readFileParamsFromCeph(@PathVariable String code) {
    ObjectMetadata objectMetadata = cephConnection.getObjectMetadata(photosBucketName, code);
    return ResponseEntity.ok(objectMetadata.getUserMetadata());
  }

  @GetMapping("/downloadFile/photo/{code}")
  public void downloadPhoto(@PathVariable String code, HttpServletResponse response)
      throws IOException {
    S3Object object = cephConnection.getObject(photosBucketName, code);
    s3ObjectToClient(object, response);
  }

  @GetMapping("/downloadFile/collation/{code}")
  public void downloadCollation(@PathVariable String code, HttpServletResponse response)
      throws IOException {
    S3Object object = cephConnection.getObject(photoCollationsBucketName, code);
    s3ObjectToClient(object, response);
  }

}
