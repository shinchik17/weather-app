package com.alexshin.weatherapp.exception.service.weatherapi;

public class ApiKeyNotFoundException extends RuntimeException {
    public ApiKeyNotFoundException(String message) {
        super(message);
    }
}
