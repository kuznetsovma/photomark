package ru.codeforensics.photomark.restapp.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.google.zxing.NotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.services.CephService;
import ru.codeforensics.photomark.services.DataMatrixService;
import ru.codeforensics.photomark.transfer.FileWithMetaTransfer;

@RestController
public class FileController {

  @Autowired
  private AmazonS3 cephConnection;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Value("${kafka.topic.files}")
  private String filesTopic;

  @Value("${system.ceph.bucketName}")
  private String bucketName;

  @Autowired
  private DataMatrixService dataMatrixService;

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

    response.setHeader("Content-Disposition",
        String.format("attachment; filename=\"%s\"", fileName));
    response.setHeader("Content-Type", "application/octet-stream");

    IOUtils.copy(object.getObjectContent(), response.getOutputStream());
  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/uploadFile/")
  public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file)
      throws IOException, NotFoundException {
    byte[] fileContent = file.getBytes();
    String code = dataMatrixService.decode(ImageIO.read(new ByteArrayInputStream(fileContent)));

    FileWithMetaTransfer fileWithMetaTransfer = new FileWithMetaTransfer();
    fileWithMetaTransfer.setCode(code);
    fileWithMetaTransfer.setFileName(file.getOriginalFilename());
    fileWithMetaTransfer.setFileData(fileContent);

    byte[] data = SerializationUtils.serialize(fileWithMetaTransfer);
    kafkaTemplate.send(filesTopic, code, data);

    return ResponseEntity.accepted().body(code);
  }

}
