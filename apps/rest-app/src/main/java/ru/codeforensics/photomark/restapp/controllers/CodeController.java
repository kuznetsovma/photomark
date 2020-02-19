package ru.codeforensics.photomark.restapp.controllers;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.codeforensics.photomark.transfer.PhotoCodeListTransfer;
import ru.codeforensics.photomark.transfer.PhotoCodeTransfer;

@RestController
public class CodeController extends AbstractController {

  @GetMapping("/code/list")
  public ResponseEntity getAll(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String line,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int limit
  ) {

    PhotoCodeListTransfer transfer = new PhotoCodeListTransfer();
    transfer.setTotal(123456789L);
    transfer.getCodes()
        .add(new PhotoCodeTransfer(1l, LocalDateTime.now(), "/asd/sdsdf", "cvdsdf", "000001"));

    return ResponseEntity.ok(transfer);
  }
}
