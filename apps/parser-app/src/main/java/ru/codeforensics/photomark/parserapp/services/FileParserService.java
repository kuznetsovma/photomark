package ru.codeforensics.photomark.parserapp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.model.cassandra.PhotoMeta;
import ru.codeforensics.photomark.services.CephService;
import ru.codeforensics.photomark.transfer.inner.PhotoTransfer;

import java.util.Base64;

@Service
public class FileParserService {

  @Autowired
  private CephService cephService;
  @Autowired
  private CassandraOperations cassandraOperations;
  @Autowired
  private ObjectMapper mapper;

  @KafkaListener(topics = "${kafka.topic.files.meta:photofiles}")
  private void onFileMetaMessage(ConsumerRecord<String, byte[]> fileRecord)
      throws JsonProcessingException {
    String json = new String(fileRecord.value());
    PhotoTransfer photoTransfer = mapper.readValue(json, PhotoTransfer.class);
    PhotoMeta photoMeta = new PhotoMeta(photoTransfer.getCode(),
        photoTransfer.getUploadedAt(),
        photoTransfer.getClientId(),
        photoTransfer.getCameraId().toString(),
        "");

    cassandraOperations.insert(photoMeta);

    // TODO: Какой именно Base64: RFC 2045, RFC 4648 или RFC 4648 URL-safe?
    byte[] imageContent = Base64.getMimeDecoder().decode(photoTransfer.getImageData());

    // TODO: Если будет код.
    //cephService.upload(photoTransfer.getCode(), imageContent);
    cephService.upload(photoTransfer.getMessageId().toString(), imageContent);
  }
}
