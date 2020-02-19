package ru.codeforensics.photomark.restapp.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.google.zxing.NotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.model.entities.PhotoCollation;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.repo.PhotoCollationRepo;
import ru.codeforensics.photomark.services.DataMatrixService;
import ru.codeforensics.photomark.services.PhotoCollationNNService;
import ru.codeforensics.photomark.transfer.PhotoCollationListTransfer;
import ru.codeforensics.photomark.transfer.PhotoCollationTransfer;
import ru.codeforensics.photomark.transfer.enums.PhotoCollationStatus;
import ru.codeforensics.photomark.transfer.inner.PhotoCollationNNTransfer;

@RestController
public class CollationController extends AbstractController {

  @Autowired
  private PhotoCollationRepo photoCollationRepo;
  @Autowired
  private DataMatrixService dataMatrixService;
  @Autowired
  private AmazonS3 cephConnection;
  @Value("${system.ceph.bucketName.photos}")
  private String photosBucketName;


  @Autowired
  private PhotoCollationNNService photoCollationNNService;

  @GetMapping("/collation/list")
  public ResponseEntity getAll(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int limit
  ) {
    Page<PhotoCollation> photoCollationPage = photoCollationRepo
        .findAll(PageRequest.of(page, limit));

    List<PhotoCollationTransfer> transfers = photoCollationPage
        .stream()
        .map(PhotoCollation::toTransfer)
        .collect(Collectors.toList());

    PhotoCollationListTransfer photoCollationListTransfer = new PhotoCollationListTransfer();
    photoCollationListTransfer.setTotal(photoCollationPage.getTotalElements());
    photoCollationListTransfer.getCollations().addAll(transfers);

    return ResponseEntity.ok(photoCollationListTransfer);
  }

  @PostMapping("/collation/upload/sync")
  public ResponseEntity uploadSync(@RequestParam("sample") MultipartFile sample)
      throws IOException {
    UserProfile userProfile = getUserSession().getUserProfile();

    try {
      byte[] sampleContent = sample.getBytes();
      String code = dataMatrixService.decode(ImageIO.read(new ByteArrayInputStream(sampleContent)));

      PhotoCollation photoCollation = new PhotoCollation();
      photoCollation.setUserProfile(userProfile);
      photoCollation.setCode(code);
      photoCollation.setStarted(LocalDateTime.now());

      S3Object object = cephConnection.getObject(photosBucketName, code);
      if (null == object) {
        photoCollation.setStatus(PhotoCollationStatus.NOT_FOUND);
      } else {
        byte[] standardContent = IOUtils.toByteArray(object.getObjectContent());
        PhotoCollationNNTransfer photoCollationNNTransfer = new PhotoCollationNNTransfer(code,
            sampleContent, standardContent);
        if (photoCollationNNService.check(photoCollationNNTransfer)) {
          photoCollation.setStatus(PhotoCollationStatus.OK);
        } else {
          photoCollation.setStatus(PhotoCollationStatus.FAKE);
        }
      }

      photoCollation.setFinished(LocalDateTime.now());
      return ResponseEntity.ok(photoCollationRepo.save(photoCollation).toTransfer());
    } catch (NotFoundException e) {
      logger.error("Photo collation processing failed", e);
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

}
