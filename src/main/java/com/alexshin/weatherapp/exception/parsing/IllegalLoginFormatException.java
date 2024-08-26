package com.alexshin.weatherapp.exception.parsing;

public class IllegalLoginFormatException extends IllegalArgumentException {

    public IllegalLoginFormatException() {
        super("Login must contain at least 4 characters of latin letters, numbers and symbols .@_.");
    }

}
