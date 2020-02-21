package ru.codeforensics.photomark.uploadapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.services.StatisticService;
import ru.codeforensics.photomark.transfer.inner.PhotoMetaTransfer;

@Controller
public class UploadController extends AbstractController {

  @Value("${kafka.topic.files}")
  private String filesTopic;
  @Value("${kafka.topic.files.meta}")
  private String filesMetaTopic;

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Autowired
  private StatisticService statisticService;

  @PostMapping("/v1/uploadPhoto")
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

    PhotoMetaTransfer photoMetaTransfer = new PhotoMetaTransfer(code,
        client.getId(), lineName,
        FilenameUtils.getExtension(file.getOriginalFilename()),
        LocalDateTime.now());
    String metaJson = mapper.writeValueAsString(photoMetaTransfer);

    kafkaTemplate.send(filesTopic, code, file.getBytes());
    kafkaTemplate.send(filesMetaTopic, code, metaJson.getBytes());

    statisticService.registerEvent(client, lineName);

    return ResponseEntity.accepted().body(code);
  }


}
