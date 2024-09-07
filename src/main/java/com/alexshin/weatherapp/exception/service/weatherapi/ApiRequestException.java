package com.alexshin.weatherapp.exception.service.weatherapi;

public class ApiRequestException extends RuntimeException{
    public ApiRequestException(String message) {
        super(message);
    }
}
