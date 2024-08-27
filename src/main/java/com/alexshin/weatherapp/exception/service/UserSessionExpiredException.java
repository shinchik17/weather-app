package com.alexshin.weatherapp.exception.service;

public class UserSessionExpiredException extends AuthenticationException {
    public UserSessionExpiredException(String message) {
        super(message);
    }

}
