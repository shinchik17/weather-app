package com.alexshin.weatherapp.exception.parsing;

public class IllegalLocationNameFormatException extends IllegalArgumentException {

    public IllegalLocationNameFormatException() {
        super("LocationName must contain at least 1 character.");
    }

}
