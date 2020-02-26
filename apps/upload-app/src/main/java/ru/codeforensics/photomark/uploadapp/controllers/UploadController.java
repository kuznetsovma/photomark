package ru.codeforensics.photomark.uploadapp.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.services.StatisticService;
import ru.codeforensics.photomark.transfer.PhotoUploadTransfer;
import ru.codeforensics.photomark.transfer.inner.PhotoTransfer;

@Controller
public class UploadController extends AbstractController {

  @Value("${kafka.topic.files}")
  private String filesTopic;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Autowired
  private StatisticService statisticService;

  @PostMapping("/v1/uploadPhoto")
  public ResponseEntity uploadFile(
      @RequestHeader(name = "X-API-Key") String apiKey,
      @RequestBody PhotoUploadTransfer transfer) throws IOException {

    Optional<Client> clientOptional = clientRepo.findByKey(apiKey);
    if (!clientOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Client client = clientOptional.get();

    if (transfer.getMessageId() == null || transfer.getCameraId() == null || transfer.getImageData() == null) {
      return  ResponseEntity.badRequest().build();
    }

    PhotoTransfer photoTransfer = new PhotoTransfer(
        client.getId(), LocalDateTime.now(), transfer.getMessageId(), transfer.getCameraId(), transfer.getCode(),
        transfer.getImageData(), transfer.getImageCenterX(), transfer.getImageCenterY(), transfer.getImageBrightness(),
        transfer.getImageContrast());
    String photoTransferJson = mapper.writeValueAsString(photoTransfer);

    kafkaTemplate.send(filesTopic, photoTransfer.getMessageId().toString(), photoTransferJson.getBytes());

    statisticService.registerEvent(client, photoTransfer.getCameraId().toString());

    return ResponseEntity.accepted().build();
  }

}
