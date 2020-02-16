package ru.codeforensics.photomark.restapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.repo.UserProfileRepo;
import ru.codeforensics.photomark.services.CryptoService;
import ru.codeforensics.photomark.transfer.UserTransfer;

@RestController
public class AdminController {

  @Autowired
  private CryptoService cryptoService;
  @Autowired
  private UserProfileRepo userProfileRepo;

  @Secured("ROLE_ADMIN")
  @PostMapping("/admin/registerUser")
  public ResponseEntity registerUser(@RequestBody UserTransfer userTransfer) {
    UserProfile userProfile = new UserProfile();
    userProfile.setEmail(userTransfer.getEmail());
    userProfile.setPasswordHash(cryptoService.hash(userTransfer.getPassword()));
    userProfile.getAuthorities().addAll(userTransfer.getAuthorities());
    return ResponseEntity.ok(userProfileRepo.save(userProfile).toTransfer());
  }
}
