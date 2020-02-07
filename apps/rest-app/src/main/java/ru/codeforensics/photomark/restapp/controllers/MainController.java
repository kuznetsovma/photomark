package ru.codeforensics.photomark.restapp.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.codeforensics.photomark.services.CephService;

@Controller
public class MainController {

  @Autowired
  private AmazonS3 cephConnection;

  @Value("${system.ceph.bucketName}")
  private String bucketName;

  @GetMapping("/readFileParams/{code}")
  public ResponseEntity readFileParamsFromCeph(@PathVariable String code) {
    ObjectMetadata objectMetadata = cephConnection.getObjectMetadata(bucketName, code);
    return ResponseEntity.ok(objectMetadata.getUserMetadata());
  }

  @GetMapping("/downloadFile/{code}")
  public void downloadFile(@PathVariable String code, HttpServletResponse response)
      throws IOException {
    S3Object object = cephConnection.getObject(bucketName, code);

    String fileName = object.getObjectMetadata().getUserMetadata().get(CephService.FILE_NAME_KEY);

    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    response.setHeader("Content-Type", "application/octet-stream");

    IOUtils.copy(object.getObjectContent(), response.getOutputStream());
  }

}
