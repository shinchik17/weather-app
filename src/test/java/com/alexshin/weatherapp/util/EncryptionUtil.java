package com.alexshin.weatherapp.util;

import org.mindrot.jbcrypt.BCrypt;

public final class EncryptionUtil {

    private EncryptionUtil(){
        throw new UnsupportedOperationException("EncryptionUtil cannot be instantiated");
    }


    public static String hashPassword(String password){
        return password;
    }

    public static boolean passwordMatches(String password, String hashedPassword){
        return password.equals(hashedPassword);
    }





}
