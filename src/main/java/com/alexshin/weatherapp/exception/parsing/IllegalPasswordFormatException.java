package com.alexshin.weatherapp.exception.parsing;

public class IllegalPasswordFormatException extends IllegalArgumentException {

    public IllegalPasswordFormatException() {
        super("Password must contain at least 8 characters of latin letters, numbers and symbols @_.!#$%^&*.");
    }

}
