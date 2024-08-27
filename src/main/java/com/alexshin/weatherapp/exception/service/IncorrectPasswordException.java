package com.alexshin.weatherapp.exception.service;

public class IncorrectPasswordException extends AuthenticationException {
    public IncorrectPasswordException(String message) {
        super(message);
    }

}
