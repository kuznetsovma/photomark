package ru.codeforensics.photomark.uploadapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import ru.codeforensics.photomark.model.repo.ClientRepo;

public abstract class AbstractController {

  @Autowired
  protected ClientRepo clientRepo;
}
