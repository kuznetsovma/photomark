package ru.codeforensics.photomark.parserapp.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import ru.codeforensics.photomark.services.CephService;
import ru.codeforensics.photomark.transfer.inner.FileWithMetaTransfer;

@Service
public class FileParserService {

  @Autowired
  private CephService cephService;

  @KafkaListener(topics = "${kafka.topic.files:photofiles}")
  private void onMessage(ConsumerRecord<String, byte[]> fileRecord) {
    System.out.println(String.format("Receive message: key=%s; value=%s", fileRecord.key(),
        fileRecord.value().toString()));

    try {
      FileWithMetaTransfer fileWithMetaTransfer = (FileWithMetaTransfer) SerializationUtils
          .deserialize(fileRecord.value());

      cephService.upload(fileWithMetaTransfer);
    } catch (IllegalArgumentException e) {
      LoggerFactory.getLogger(getClass()).info("Old data version:", e);
    }
  }
}
