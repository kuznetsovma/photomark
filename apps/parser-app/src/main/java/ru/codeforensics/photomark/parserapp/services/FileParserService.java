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
import ru.codeforensics.photomark.transfer.inner.PhotoMetaTransfer;

@Service
public class FileParserService {

  @Autowired
  private CephService cephService;
  @Autowired
  private CassandraOperations cassandraOperations;
  @Autowired
  private ObjectMapper mapper;

  @KafkaListener(topics = "${kafka.topic.files:photofiles}")
  private void onFileMessage(ConsumerRecord<String, byte[]> fileRecord) {
    System.out.println(String.format("Receive message: key=%s; value=%s", fileRecord.key(),
        fileRecord.value().toString()));

    cephService.upload(fileRecord.key(), fileRecord.value());
  }

  @KafkaListener(topics = "${kafka.topic.files.meta:photofiles-meta}")
  private void onFileMetaMessage(ConsumerRecord<String, byte[]> fileMetaRecord)
      throws JsonProcessingException {
    String json = new String(fileMetaRecord.value());
    PhotoMetaTransfer photoMetaTransfer = mapper.readValue(json, PhotoMetaTransfer.class);
    PhotoMeta photoMeta = new PhotoMeta(photoMetaTransfer.getCode(),
        photoMetaTransfer.getUploaded(),
        photoMetaTransfer.getClientId(),
        photoMetaTransfer.getLineName(),
        photoMetaTransfer.getExt());

    cassandraOperations.insert(photoMeta);
  }
}
