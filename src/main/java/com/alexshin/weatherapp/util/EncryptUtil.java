package com.alexshin.weatherapp.util;

import org.mindrot.jbcrypt.BCrypt;

public final class EncryptUtil {
    private static final String salt = BCrypt.gensalt();

    private EncryptUtil(){
        throw new UnsupportedOperationException("EncryptUtil cannot be instantiated");
    }


    public static String hashPassword(String password){
        return BCrypt.hashpw(password, salt);
    }

    public static boolean passwordMatches(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }





}
