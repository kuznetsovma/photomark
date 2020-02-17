package ru.codeforensics.photomark.restapp.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.entities.UserSession;
import ru.codeforensics.photomark.model.repo.UserProfileRepo;
import ru.codeforensics.photomark.model.repo.UserSessionRepo;
import ru.codeforensics.photomark.restapp.security.UserSessionDetails;
import ru.codeforensics.photomark.services.CryptoService;
import ru.codeforensics.photomark.services.UserSessionService;
import ru.codeforensics.photomark.transfer.LoginTransfer;

@RestController
public class LoginController {

  @Autowired
  private CryptoService cryptoService;
  @Autowired
  private UserProfileRepo userProfileRepo;
  @Autowired
  private UserSessionService userSessionService;
  @Autowired
  private UserSessionRepo userSessionRepo;

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginTransfer transfer) {
    String pwdHash = cryptoService.hash(transfer.getPassword());
    Optional<UserProfile> userProfileOptional = userProfileRepo
        .findByEmailAndPasswordHash(transfer.getEmail(), pwdHash);

    if (!userProfileOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserSession session = userSessionService.createNewUserSession(userProfileOptional.get());
    return ResponseEntity.ok(session.toTransfer());
  }


  @PostMapping("/logout")
  public ResponseEntity logout(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    if (principal instanceof UserSessionDetails) {
      UserSessionDetails userSessionDetails = (UserSessionDetails) principal;
      userSessionRepo.delete(userSessionDetails.getUserSession());
      return ResponseEntity.ok().build();
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
}
