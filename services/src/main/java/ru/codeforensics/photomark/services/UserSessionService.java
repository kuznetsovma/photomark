package ru.codeforensics.photomark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.codeforensics.photomark.model.entities.UserProfile;
import ru.codeforensics.photomark.model.entities.UserSession;
import ru.codeforensics.photomark.model.repo.UserSessionRepo;
import ru.codeforensics.photomark.services.utils.CommonUtil;

@Service
public class UserSessionService {

  @Value("${app.session.maxAgeDays}")
  private int maxAgeDays;

  @Autowired
  private UserSessionRepo userSessionRepo;

  public UserSession createNewUserSession(UserProfile userProfile) {
    UserSession session = new UserSession();
    session.setUserProfile(userProfile);
    session.setToken(CommonUtil.randomAlphaNumericString(32));
    session.setExpired(session.getCreated().plusDays(maxAgeDays));
    return userSessionRepo.save(session);
  }
}
