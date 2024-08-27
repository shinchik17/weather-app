package com.alexshin.weatherapp.exception.service;

public class NoSuchUserException extends AuthenticationException {
    public NoSuchUserException(String message) {
        super(message);
    }

}
