package ru.codeforensics.photomark.services.utils;

import java.util.Random;

public class CommonUtil {

  public static final Random RANDOM = new Random(System.nanoTime());

  public static String randomNumberString(int length) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; i++) {
      result.append("" + RANDOM.nextInt(10));
    }
    return result.toString();
  }

  public static String randomAlphaNumericString(int n) {
    String alphaNumericAlphaBet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "0123456789"
        + "abcdefghijklmnopqrstuvxyz";

    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      int index = RANDOM.nextInt(alphaNumericAlphaBet.length());
      sb.append(alphaNumericAlphaBet.charAt(index));
    }

    return sb.toString();
  }
}
