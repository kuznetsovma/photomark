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
import org.springframework.stereotype.Controller;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.transfer.FileWithMetaTransfer;

@Controller
public class MainController {

  @Value("${kafka.topic.files}")
  private String filesTopic;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Autowired
  private KafkaAdmin kafkaAdmin;

  @PostMapping("/createTopic/{name}")
  public ResponseEntity createTopic(@PathVariable String name) {
    AdminClient client = AdminClient.create(kafkaAdmin.getConfig());
    CreateTopicsResult topics = client
        .createTopics(Arrays.asList(new NewTopic(name, 10, (short) 2)));
    client.close();

    return ResponseEntity.ok(topics.values());
  }

  @PostMapping("/uploadFile")
  public ResponseEntity uploadFile(
      @RequestHeader(name = "id_client") Long clientId,
      @RequestHeader(name = "line_name") String lineName,
      @RequestHeader(name = "km") String code,
      @RequestParam("file") MultipartFile file) throws IOException {

    FileWithMetaTransfer fileWithMetaTransfer = new FileWithMetaTransfer();
    fileWithMetaTransfer.setClientId(clientId);
    fileWithMetaTransfer.setLineName(lineName);
    fileWithMetaTransfer.setCode(code);
    fileWithMetaTransfer.setFileName(file.getOriginalFilename());
    fileWithMetaTransfer.setFileData(file.getBytes());

    byte[] data = SerializationUtils.serialize(fileWithMetaTransfer);
    kafkaTemplate.send(filesTopic, code, data);

    return ResponseEntity.ok().build();
  }
}
