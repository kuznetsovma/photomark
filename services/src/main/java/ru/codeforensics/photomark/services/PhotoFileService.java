package ru.codeforensics.photomark.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codeforensics.photomark.model.entities.Photo;
import ru.codeforensics.photomark.model.repo.ClientRepo;
import ru.codeforensics.photomark.model.repo.PhotoRepo;
import ru.codeforensics.photomark.transfer.FileWithMetaTransfer;

@Service
public class PhotoFileService {

  @Autowired
  private PhotoRepo photoRepo;
  @Autowired
  private ClientRepo clientRepo;

  @Autowired
  private CephService cephService;

  @Transactional
  public void save(FileWithMetaTransfer fileWithMetaTransfer) {
    String code = fileWithMetaTransfer.getCode();

    Optional<Photo> photoOptional = photoRepo.findByCode(code);
    if (!photoOptional.isPresent()) {
      Photo photo = new Photo();
      photo.setClient(clientRepo.findById(fileWithMetaTransfer.getClientId()).orElse(null));
      photo.setCode(code);
      photo.setLineId(fileWithMetaTransfer.getLineName());
      photoRepo.save(photo);
    }

    cephService.upload(fileWithMetaTransfer);
  }
}
