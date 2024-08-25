package com.alexshin.weatherapp.exception.service;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String message) {
        super(message);
    }

    public IncorrectPasswordException(Throwable cause) {
        super(cause);
    }
}
