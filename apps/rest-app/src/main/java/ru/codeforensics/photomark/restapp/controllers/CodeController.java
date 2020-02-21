package ru.codeforensics.photomark.restapp.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.codeforensics.photomark.model.cassandra.PhotoMeta;
import ru.codeforensics.photomark.model.cassandra.PhotoMetaRepo;
import ru.codeforensics.photomark.transfer.PhotoCodeListTransfer;
import ru.codeforensics.photomark.transfer.PhotoCodeTransfer;

@RestController
public class CodeController extends AbstractController {

  @Autowired
  private PhotoMetaRepo photoMetaRepo;

  @GetMapping("/code/list")
  public ResponseEntity getAll(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String line,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int limit
  ) {

    Slice<PhotoMeta> photoMetaSlice = photoMetaRepo.findAll(PageRequest.of(page, limit));
    List<PhotoCodeTransfer> codes = new ArrayList<>();
    for (PhotoMeta photoMeta : photoMetaSlice.getContent()) {
      PhotoCodeTransfer transfer = new PhotoCodeTransfer();
      transfer.setCode(photoMeta.getCode());
      transfer.setDateTime(photoMeta.getDateTime());
      transfer.setLine(photoMeta.getLineName());
      transfer.setStandard(String.format("/downloadFile/photo/%s", photoMeta.getCode()));
      codes.add(transfer);
    }

    PhotoCodeListTransfer transfer = new PhotoCodeListTransfer();
    transfer.setTotal(photoMetaSlice.getNumberOfElements());
    transfer.getCodes().addAll(codes);

    return ResponseEntity.ok(transfer);
  }
}
