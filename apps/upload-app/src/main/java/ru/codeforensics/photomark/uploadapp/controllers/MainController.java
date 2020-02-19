package ru.codeforensics.photomark.uploadapp.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.model.repo.ClientRepo;
import ru.codeforensics.photomark.services.StatisticService;
import ru.codeforensics.photomark.transfer.enums.UploadStatus;
import ru.codeforensics.photomark.transfer.inner.FileWithMetaTransfer;

@Controller
public class MainController {

  @Value("${kafka.topic.files}")
  private String filesTopic;

  @Value("${system.ceph.bucketName.photos}")
  private String photosBucketName;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Autowired
  private ClientRepo clientRepo;

  @Autowired
  private StatisticService statisticService;

  @Autowired
  private AmazonS3 cephConnection;

  @PostMapping("/api/v1/photos")
  public ResponseEntity uploadFile(
      @RequestHeader(name = "line_name") String lineName,
      @RequestHeader(name = "km") String code,
      @RequestHeader(name = "X-API-Key") String xApiKey,
      @RequestParam("file") MultipartFile file) throws IOException {

    Optional<Client> clientOptional = clientRepo.findByKey(xApiKey);
    if (!clientOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Client client = clientOptional.get();

    FileWithMetaTransfer fileWithMetaTransfer = new FileWithMetaTransfer();
    fileWithMetaTransfer.setClientId(client.getId());
    fileWithMetaTransfer.setLineName(lineName);
    fileWithMetaTransfer.setCode(code);
    fileWithMetaTransfer.setFileName(file.getOriginalFilename());
    fileWithMetaTransfer.setFileData(file.getBytes());

    byte[] data = SerializationUtils.serialize(fileWithMetaTransfer);
    kafkaTemplate.send(filesTopic, code, data);

    statisticService.registerEvent(client, lineName);

    return ResponseEntity.accepted().body(code);
  }

  @GetMapping("/api/v1/checkStatus")
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
