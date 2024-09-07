package com.alexshin.weatherapp.exception.parsing;

public class IllegalCoordinatesFormatException extends IllegalArgumentException {

    public IllegalCoordinatesFormatException() {
        super("Illegal coordinate format.");
    }

}
