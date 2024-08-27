package com.alexshin.weatherapp.exception.service;

public class SuchUserExistsException extends AuthenticationException {
    public SuchUserExistsException(String message) {
        super(message);
    }

}
