package ru.codeforensics.photomark.uploadapp.controllers;

import java.io.IOException;
import java.util.Arrays;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.services.StatisticService;
import ru.codeforensics.photomark.transfer.FileWithMetaTransfer;
import ru.codeforensics.photomark.uploadapp.security.ClientDetails;

@Controller
public class MainController {

  @Value("${kafka.topic.files}")
  private String filesTopic;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Autowired
  private KafkaAdmin kafkaAdmin;

  @Autowired
  private StatisticService statisticService;

  @PostMapping("/createTopic/{name}")
  public ResponseEntity createTopic(@PathVariable String name) {
    AdminClient client = AdminClient.create(kafkaAdmin.getConfig());
    CreateTopicsResult topics = client
        .createTopics(Arrays.asList(new NewTopic(name, 10, (short) 2)));
    client.close();

    return ResponseEntity.ok(topics.values());
  }

  @PostMapping("/api/v1/photos")
  public ResponseEntity uploadFile(
      @RequestHeader(name = "line_name") String lineName,
      @RequestHeader(name = "km") String code,
      @RequestParam("file") MultipartFile file,
      Authentication authentication) throws IOException {

    Client client = ((ClientDetails) authentication.getPrincipal()).getClient();

    FileWithMetaTransfer fileWithMetaTransfer = new FileWithMetaTransfer();
    fileWithMetaTransfer.setClientId(client.getId());
    fileWithMetaTransfer.setLineName(lineName);
    fileWithMetaTransfer.setCode(code);
    fileWithMetaTransfer.setFileName(file.getOriginalFilename());
    fileWithMetaTransfer.setFileData(file.getBytes());

    byte[] data = SerializationUtils.serialize(fileWithMetaTransfer);
    kafkaTemplate.send(filesTopic, code, data);

    statisticService.registerEvent(client, lineName);

    return ResponseEntity.accepted().build();
  }
}
