package com.alexshin.weatherapp.util;

import org.mindrot.jbcrypt.BCrypt;

public final class EncryptionUtil {
    private static final String salt = System.getenv(PropertiesUtil.getProperty("encryption.var.name"));

    private EncryptionUtil(){
        throw new UnsupportedOperationException("EncryptionUtil cannot be instantiated");
    }


    public static String hashPassword(String password){
        return BCrypt.hashpw(password, salt);
    }

    public static boolean passwordMatches(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }





}
