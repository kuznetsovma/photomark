package ru.codeforensics.photomark.restapp.controllers;

import com.google.zxing.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.util.SerializationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.codeforensics.photomark.model.entities.PhotoCollation;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.repo.PhotoCollationRepo;
import ru.codeforensics.photomark.restapp.security.UserSessionDetails;
import ru.codeforensics.photomark.services.DataMatrixService;
//import ru.codeforensics.photomark.transfer.PhotoCollationTaskTransfer;
import ru.codeforensics.photomark.transfer.PhotoCollationTransfer;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/photo_collations")
public class PhotoCollationController {

  @Autowired
  private DataMatrixService dataMatrixService;

//  @Autowired
//  private KafkaTemplate<Long, byte[]> taskKafkaTemplate;

  @Value("${kafka.topic.photo_collation_tasks}")
  private String tasksTopic;

  @Autowired
  private PhotoCollationRepo photoCollationRepo;

  private static Logger logger = LoggerFactory.getLogger(PhotoCollationController.class);

  @GetMapping("/{id}")
  public ResponseEntity get(@PathVariable("id") Long id, Authentication authentication)
      throws ResponseStatusException {
    UserProfile userProfile = getUserProfile(authentication);

    Optional<PhotoCollation> collation = photoCollationRepo.findByIdAndUserProfile(id, userProfile);
    if (!collation.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    return ResponseEntity.ok(collation.get().toTransfer());
  }

  @GetMapping("")
  public ResponseEntity getAll(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      Authentication authentication)
      throws ResponseStatusException {
    Pageable paging;
    try { paging = PageRequest.of(page, 10); }
    catch (IllegalArgumentException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    UserProfile userProfile = getUserProfile(authentication);

    List<PhotoCollationTransfer> transfers = photoCollationRepo.findAllByUserProfile(userProfile, paging).stream()
        .map(PhotoCollation::toTransfer).collect(Collectors.toList());

    return ResponseEntity.ok(transfers);
  }

  @PostMapping("")
  public ResponseEntity create(
      @RequestParam("sample") MultipartFile sample,
      Authentication authentication)
      throws IOException, ResponseStatusException {
    UserProfile userProfile = getUserProfile(authentication);

    PhotoCollation photoCollation = new PhotoCollation();
    photoCollation.setUserProfile(userProfile);
    photoCollationRepo.save(photoCollation);

    photoCollation.setStartedAt(LocalDateTime.now());
    photoCollation.setStatus(PhotoCollation.STATUS_PROCESSING);
    photoCollationRepo.save(photoCollation);

// TODO: Store sample photo to Ceph.
//
//    photoCollation.setSampleKey(sampleKey);

    String sampleCode = null;
    try {
      byte[] sampleContent = sample.getBytes();
      sampleCode = dataMatrixService.decode(ImageIO.read(new ByteArrayInputStream(sampleContent)));
    }
    catch (NotFoundException exception) {
      photoCollation.setStatus(PhotoCollation.STATUS_SUCCEED);
      photoCollation.setResult(PhotoCollation.RESULT_FAILED);
    }
    catch (Exception exception) {
      logger.error("Photo collation processing failed: " + exception.getMessage(), exception);
      photoCollation.setStatus(PhotoCollation.STATUS_FAILED);
    }

    if (photoCollation.getStatus() == PhotoCollation.STATUS_PROCESSING) {
      photoCollation.setSampleCode(sampleCode);

// TODO: Find original photo in Ceph by code.
//
//    photoCollation.setOriginalKey(originalKey);

      photoCollation.setStatus(PhotoCollation.STATUS_SUCCEED);
    }

    photoCollation.setFinishedAt(LocalDateTime.now());
    photoCollationRepo.save(photoCollation);

// TODO: Put photoCollation to Kafka.
//
//    PhotoCollationTaskTransfer task = new PhotoCollationTaskTransfer();
//    task.setPhotoCollationId(photoCollation.getId());
//    task.setSampleFileData(sample.getBytes());
//    task.setSampleFileName(sample.getOriginalFilename());
//
//    byte[] data = SerializationUtils.serialize(task);
//    taskKafkaTemplate.send(tasksTopic, photoCollation.getId(), data);

    return ResponseEntity
        .created(URI.create(MvcUriComponentsBuilder.fromMappingName("PCC#get").arg(0, photoCollation.getId()).build()))
        .body(photoCollation.toTransfer());
  }

  private UserProfile getUserProfile(Authentication authentication) {
    Object principal = authentication.getPrincipal();

    if (principal instanceof UserSessionDetails) {
      UserSessionDetails userSessionDetails = (UserSessionDetails) principal;
      return userSessionDetails.getUserSession().getUserProfile();
    }

    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
