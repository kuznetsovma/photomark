package ru.codeforensics.photomark.restapp.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.codeforensics.photomark.model.entities.Client;
import ru.codeforensics.photomark.model.repo.ClientRepo;
import ru.codeforensics.photomark.transfer.ClientTransfer;

@RestController
public class ClientController {

  @Autowired
  private ClientRepo clientRepo;

  @Secured("ROLE_ADMIN")
  @GetMapping("/clients")
  public ResponseEntity getAll() {
    List<ClientTransfer> clientTransfers = clientRepo.findAll().stream().map(Client::toTransfer)
        .collect(Collectors.toList());
    return ResponseEntity.ok(clientTransfers);
  }


}
