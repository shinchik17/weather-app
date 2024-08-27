package com.alexshin.weatherapp.exception.service;

public class NoSuchUserSessionException extends AuthenticationException {
    public NoSuchUserSessionException(String message) {
        super(message);
    }

}
