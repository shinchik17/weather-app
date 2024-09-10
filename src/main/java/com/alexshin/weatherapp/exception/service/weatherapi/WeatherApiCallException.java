package com.alexshin.weatherapp.exception.service.weatherapi;

public class WeatherApiCallException extends RuntimeException{
    public WeatherApiCallException(String message) {
        super(message);
    }
}
