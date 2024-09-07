package com.alexshin.weatherapp.exception.service;

public class SuchLocationExistsException extends RuntimeException {
    public SuchLocationExistsException(String message) {
        super(message);
    }

}
