package com.capgemini.bedwards.almon.almondatastore.util;

public class Constants {
    public static final String PASSWORD_REGEX = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&\\.])[A-Za-z\\d@$!%*?&\\.]{8,}";
    public static final String PASSWORD_INVALID_MESSAGE = "Password must have a minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character.";
}
