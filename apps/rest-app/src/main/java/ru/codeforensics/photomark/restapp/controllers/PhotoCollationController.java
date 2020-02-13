package ru.codeforensics.photomark.restapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.codeforensics.photomark.model.entities.PhotoCollation;
import ru.codeforensics.photomark.model.repo.PhotoCollationRepo;
import ru.codeforensics.photomark.transfer.CollationTransfer;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/photo-collations")
public class PhotoCollationController {

  @Autowired
  private PhotoCollationRepo photoCollationRepo;

  @GetMapping("/{id}")
  public ResponseEntity get(@PathVariable Long id)
      throws ResponseStatusException {
    Optional<PhotoCollation> collation = photoCollationRepo.findById(id);
    if (!collation.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(collation.get().toTransfer());
  }

  @GetMapping("")
  public ResponseEntity getAll() {
    List<CollationTransfer> transfers = photoCollationRepo.findAll().stream().map(PhotoCollation::toTransfer)
        .collect(Collectors.toList());
    return ResponseEntity.ok(transfers);
  }

  @PostMapping("")
  public ResponseEntity create() {
    PhotoCollation collation = new PhotoCollation();
    photoCollationRepo.save(collation);
    return ResponseEntity
        .created(URI.create(MvcUriComponentsBuilder.fromMappingName("PCC#get").arg(0, collation.getId()).build()))
        .body(collation.toTransfer());
  }

}
