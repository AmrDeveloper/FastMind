package com.amrdeveloper.fastmind.utils;

import android.util.Patterns;

/**
 * Class to check if data attribute is valid or not
 */
public class DataValidation {

    /**
     * Pattern to match player username
     * Rules :
     * 1 - Text Length >= 5
     * 2 - Valid characters: a-z, A-Z, 0-9, points, dashes and underscores.
     */
    private final static String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{5,}$";

    /**
     * Pattern to match player Password
     * Rules :
     * 1 - Text Length >= 8
     * 2 - Valid characters: a-z, A-Z, 0-9, points, dashes and underscores.
     */
    private final static String PASSWORD_PATTERN = "^[a-zA-Z0-9._-]{8,}$";

    private DataValidation() {
    }

    /**
     * @param email : Player email address
     * @return : true if email address is valid syntax and match rules
     */
    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * @param username : player username
     * @return : true if player username is valid syntax and match rules
     */
    public static boolean isUsernameValid(String username) {
        return username.matches(USERNAME_PATTERN);
    }

    /**
     * @param password : player password
     * @return : true if player password is valid syntax and match rules
     */
    public static boolean isPasswordValid(String password) {
        return password.matches(PASSWORD_PATTERN);
    }
}
