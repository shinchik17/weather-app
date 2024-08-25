package com.alexshin.weatherapp.exception.service;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException(Throwable cause) {
        super(cause);
    }
}
