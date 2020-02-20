package ru.codeforensics.photomark.restapp.controllers;

import com.google.zxing.NotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.repo.ClientRepo;
import ru.codeforensics.photomark.model.repo.UserProfileRepo;
import ru.codeforensics.photomark.services.CryptoService;
import ru.codeforensics.photomark.services.DataMatrixService;
import ru.codeforensics.photomark.transfer.admin.ClientTransfer;
import ru.codeforensics.photomark.transfer.admin.UserTransfer;

@RestController
public class AdminController extends AbstractController {

  @Autowired
  private ClientRepo clientRepo;
  @Autowired
  private CryptoService cryptoService;
  @Autowired
  private UserProfileRepo userProfileRepo;

  @Autowired
  private DataMatrixService dataMatrixService;

  @Value("${kafka.topic.files}")
  private String photoFilesTopic;

  @Autowired
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Secured("ROLE_ADMIN")
  @PostMapping("/admin/registerUser")
  public ResponseEntity registerUser(@RequestBody UserTransfer userTransfer) {
    UserProfile userProfile = new UserProfile();
    userProfile.setEmail(userTransfer.getEmail());
    userProfile.setPasswordHash(cryptoService.hash(userTransfer.getPassword()));
    userProfile.getAuthorities().addAll(userTransfer.getAuthorities());
    return ResponseEntity.ok(userProfileRepo.save(userProfile).toTransfer());
  }

  @Secured("ROLE_ADMIN")
  @GetMapping("/admin/client/list")
  public ResponseEntity getAll() {
    List<ClientTransfer> clientTransfers = clientRepo.findAll().stream().map(Client::toTransfer)
        .collect(Collectors.toList());
    return ResponseEntity.ok(clientTransfers);
  }


  @Secured("ROLE_ADMIN")
  @PostMapping("/admin/uploadPhotoFile/")
  public ResponseEntity uploadPhotoFile(
      @RequestParam("clientId") Long clientId,
      @RequestParam("lineName") String lineName,
      @RequestParam("file") MultipartFile file)
      throws IOException, NotFoundException {
    byte[] fileContent = file.getBytes();
    String code = dataMatrixService.decode(ImageIO.read(new ByteArrayInputStream(fileContent)));

    kafkaTemplate.send(photoFilesTopic, code, fileContent);

    return ResponseEntity.accepted().body(code);
  }
}