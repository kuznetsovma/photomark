package ru.codeforensics.photomark.parserapp;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import ru.codeforensics.photomark.model.entities.Photo;
import ru.codeforensics.photomark.model.repo.ClientRepo;
import ru.codeforensics.photomark.model.repo.PhotoRepo;
import ru.codeforensics.photomark.transfer.FileMetaTransfer;

@Service
public class FileParserService {

  @Autowired
  private PhotoRepo photoRepo;
  @Autowired
  private ClientRepo clientRepo;

  /*
  @Autowired
  private CephUploader cephUploader;
  */

  @KafkaListener(topics = "${kafka.topic.files:photofiles}")
  private void onMessage(ConsumerRecord<String, byte[]> fileRecord) {
    System.out.println(String.format("Receive message: key=%s; value=%s", fileRecord.key(),
        fileRecord.value().toString()));

    FileMetaTransfer fileMetaTransfer = (FileMetaTransfer) SerializationUtils
        .deserialize(fileRecord.value());

    // throw new RuntimeException("failed");

    Photo photo = new Photo();
    photo.setClient(clientRepo.findById(fileMetaTransfer.getClientId()).orElse(null));
    photo.setLineId(fileMetaTransfer.getLineName());
    photo.setCode(fileMetaTransfer.getCode());
    // photo.setFileUrl(cephUploader.upload(fileRecord.value()));
    photoRepo.save(photo);
  }
}
