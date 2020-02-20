package ru.codeforensics.photomark.restapp.controllers;

import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.codeforensics.photomark.model.entities.UserSession;
import ru.codeforensics.photomark.restapp.security.UserSessionDetails;

public abstract class AbstractController {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected UserSession getUserSession() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

    if (principal instanceof UserSessionDetails) {
      UserSessionDetails userSessionDetails = (UserSessionDetails) principal;
      return userSessionDetails.getUserSession();
    }

    return null;
  }

  protected void s3ObjectToClient(S3Object object, HttpServletResponse response)
      throws IOException {
    response.setHeader("Content-Disposition",
        String.format("attachment; filename=\"%s\"", object.getKey()));
    response.setHeader("Content-Type", "application/octet-stream");

    IOUtils.copy(object.getObjectContent(), response.getOutputStream());
  }
}
