package ru.codeforensics.photomark.restapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.codeforensics.photomark.model.entities.PhotoCollation;
import ru.codeforensics.photomark.model.repo.PhotoCollationRepo;
import ru.codeforensics.photomark.transfer.PhotoCollationTaskTransfer;
import ru.codeforensics.photomark.transfer.PhotoCollationTransfer;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/photo_collations")
public class PhotoCollationController {

//  @Autowired
//  private KafkaTemplate<Long, byte[]> taskKafkaTemplate;

  @Value("${kafka.topic.photo_collation_tasks}")
  private String tasksTopic;

  @Autowired
  private PhotoCollationRepo photoCollationRepo;

  @GetMapping("/{id}")
  public ResponseEntity get(@PathVariable("id") Long id)
      throws ResponseStatusException {
    Optional<PhotoCollation> collation = photoCollationRepo.findById(id);
    if (!collation.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(collation.get().toTransfer());
  }

  @GetMapping("")
  public ResponseEntity getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page)
      throws ResponseStatusException {
    Pageable paging;
    try { paging = PageRequest.of(page, 10); }
    catch (IllegalArgumentException exception) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    List<PhotoCollationTransfer> transfers = photoCollationRepo.findAll(paging).stream().map(PhotoCollation::toTransfer)
        .collect(Collectors.toList());
    return ResponseEntity.ok(transfers);
  }

  @PostMapping("")
  public ResponseEntity create(@RequestParam("sample") MultipartFile sample)
      throws IOException {
    PhotoCollation collation = new PhotoCollation();
    photoCollationRepo.save(collation);

    PhotoCollationTaskTransfer task = new PhotoCollationTaskTransfer();
    task.setPhotoCollationId(collation.getId());
    task.setSampleFileData(sample.getBytes());
    task.setSampleFileName(sample.getOriginalFilename());

// TODO: Put collation to Kafka.
//    byte[] data = SerializationUtils.serialize(task);
//    taskKafkaTemplate.send(tasksTopic, collation.getId(), data);

    return ResponseEntity
        .created(URI.create(MvcUriComponentsBuilder.fromMappingName("PCC#get").arg(0, collation.getId()).build()))
        .body(collation.toTransfer());
  }

}
