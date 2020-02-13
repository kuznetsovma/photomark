package ru.codeforensics.photomark.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CryptoService {

  private MessageDigest digest = MessageDigest.getInstance("SHA-512");

  @Value("${app.pwdhash.salt}")
  private String salt;

  public CryptoService() throws NoSuchAlgorithmException {
  }

  public String hash(String data) {
    byte[] messageDigest = digest.digest((data + salt).getBytes());
    return Hex.encodeHexString(messageDigest);
  }
}
