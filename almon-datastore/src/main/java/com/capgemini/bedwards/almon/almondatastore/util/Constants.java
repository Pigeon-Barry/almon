package com.capgemini.bedwards.almon.almondatastore.util;

import java.time.LocalDateTime;
import java.time.Month;

public class Constants {

  public static final String PASSWORD_REGEX = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\.])[A-Za-z\\d@$!%*?&\\.]{8,}";
  public static final String PASSWORD_INVALID_MESSAGE = "Password must have a minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.";

  public static final String SERVICE_KEY_REGEX = "[A-Z0-9-]{3,16}";
  public static final String SERVICE_KEY_REGEX_INVALID_MESSAGE = "Service key must contain between 3 and 16 characters. Can can only contain letters, numbers and dashes";

  public static final String MONITOR_KEY_REGEX = "[A-Z0-9_]{3,16}";
  public static final String MONITOR_KEY_REGEX_INVALID_MESSAGE = "Monitor key must contain between 3 and 16 characters. Can can only contain letters, numbers and underscores";

  public static final int MONITOR_ID_MAX_LENGTH = 16;
  public static final int SERVICE_ID_MAX_LENGTH = 16;
  public static final int ALERT_SHORT_MAX_LENGTH = 1000;
  public static final int ALERT_LONG_MAX_LENGTH = 3000;
  public static String API_KEY_HEADER = "x-api-key";

  public static LocalDateTime DEFAULT_MONITOR_PREVENT_UNTIL = LocalDateTime.of(0, Month.JANUARY, 1, 0, 0);

}
