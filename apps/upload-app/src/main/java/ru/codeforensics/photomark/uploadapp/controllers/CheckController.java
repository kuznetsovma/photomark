package ru.codeforensics.photomark.uploadapp.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.transfer.enums.UploadStatus;

@Controller
public class CheckController extends AbstractController {

  @Autowired
  private AmazonS3 cephConnection;
  @Value("${system.ceph.bucketName.photos}")
  protected String photosBucketName;

  @GetMapping("/v1/checkStatus")
  public ResponseEntity checkStatus(
      @RequestHeader("X-API-Key") String xApiKey,
      @RequestParam(name = "km") String code) {

    Optional<Client> clientOptional = clientRepo.findByKey(xApiKey);
    if (!clientOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      cephConnection.getObjectMetadata(photosBucketName, code);
      return ResponseEntity.ok(UploadStatus.UPLOADED);
    } catch (AmazonS3Exception s3e) {
      if (404 == s3e.getStatusCode()) {
        return ResponseEntity.ok(UploadStatus.IN_QUEUE);
      } else {
        throw s3e;
      }
    }
  }
}
