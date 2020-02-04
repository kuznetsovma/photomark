package ru.codeforensics.photomark.uploadapp.security;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class APIKeyFilter extends AbstractPreAuthenticatedProcessingFilter {

  private String principalRequestHeader = "X-API-Key";

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    return request.getHeader(principalRequestHeader);
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return null;
  }

}
