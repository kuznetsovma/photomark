package ru.codeforensics.photomark.restapp.controllers;

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
import ru.codeforensics.photomark.transfer.PhotoCollationTaskTransfer;
import ru.codeforensics.photomark.transfer.PhotoCollationTransfer;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/photo_collations")
public class PhotoCollationController {

//  @Autowired
//  private KafkaTemplate<Long, byte[]> taskKafkaTemplate;

  @Value("${kafka.topic.photo_collation_tasks}")
  private String tasksTopic;

  @Autowired
  private PhotoCollationRepo photoCollationRepo;

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

    PhotoCollationTaskTransfer task = new PhotoCollationTaskTransfer();
    task.setPhotoCollationId(photoCollation.getId());
    task.setSampleFileData(sample.getBytes());
    task.setSampleFileName(sample.getOriginalFilename());

// TODO: Put photoCollation to Kafka.
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
