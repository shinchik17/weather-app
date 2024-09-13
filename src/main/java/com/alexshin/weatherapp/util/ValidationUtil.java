package com.alexshin.weatherapp.util;

public final class ValidationUtil {
    private final static String LOGIN_VALIDATE_PATTERN = "[a-zA-Z0-9@_.]{4,}";
    private final static String PASSWORD_VALIDATE_PATTERN = "[a-zA-Z0-9@_.!#$%^&*]{8,}";
    private final static String LOCATION_VALIDATE_PATTERN = "[a-zA-zА-Яа-я0-9 \\-'.]+";
    private final static String LOCATION_SOFT_VALIDATE_PATTERN = "\\S+";



    private ValidationUtil() {
        throw new UnsupportedOperationException("PropertiesUtil cannot be instantiated");
    }


    public static boolean isValidLogin(String loginStr){
        return loginStr != null && loginStr.matches(LOGIN_VALIDATE_PATTERN);
    }

    public static boolean isValidPassword(String passwordStr){
        return passwordStr != null && passwordStr.matches(PASSWORD_VALIDATE_PATTERN);
    }

    public static boolean isValidLocationName(String locationName_str){
        return locationName_str != null && locationName_str.trim().matches(LOCATION_SOFT_VALIDATE_PATTERN);
    }




}
