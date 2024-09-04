package com.alexshin.weatherapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WeatherService {
    private final String API_KEY;
    private static String API_SERVICE_URL = "https://api.openweathermap.org/data/2.5/";
    private static final String searchByNameString = API_SERVICE_URL + "weather?q=%s&appid=%s";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public WeatherService() {
        // TODO: replace with System.getenv
//        API_KEY = System.getenv("WEATHER_API_KEY");
        API_KEY = System.getenv("WEATHER_API_KEY");


    }


    public void findLocationsByName(String name) throws URISyntaxException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(searchByNameString.formatted(name, API_KEY)))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5L))
                .build();

//        HttpResponse response = client.send(request, HttpResponse.BodyHandlers);


    }


}
