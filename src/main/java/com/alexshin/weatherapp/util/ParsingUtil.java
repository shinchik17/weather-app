package com.alexshin.weatherapp.util;


import com.alexshin.weatherapp.exception.parsing.IllegalCoordinatesFormatException;
import com.alexshin.weatherapp.exception.parsing.IllegalLocationNameFormatException;
import com.alexshin.weatherapp.exception.parsing.IllegalLoginFormatException;
import com.alexshin.weatherapp.exception.parsing.IllegalPasswordFormatException;

import java.math.BigDecimal;

import static com.alexshin.weatherapp.util.ValidationUtil.*;

public class ParsingUtil {


    private ParsingUtil() {
    }

    public static String parseLogin(String login) {
        if (isValidLogin(login)) {
            return login;
        } else {
            throw new IllegalLoginFormatException();
        }
    }


    public static String parsePassword(String password) {
        if (isValidPassword(password)) {
            return password;
        } else {
            throw new IllegalPasswordFormatException();
        }
    }


    public static String parseLocationName(String locationName) {
        if (isValidLocationName(locationName)) {
            return locationName.trim();
        } else {
            throw new IllegalLocationNameFormatException();
        }

    }

    public static BigDecimal parseCoord(String coord){
        try {
            return new BigDecimal(coord);
        } catch (NumberFormatException e){
            throw new IllegalCoordinatesFormatException();
        }
    }


}
