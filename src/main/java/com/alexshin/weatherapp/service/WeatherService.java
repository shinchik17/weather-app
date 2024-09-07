package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.exception.service.ApiKeyNotFoundException;
import com.alexshin.weatherapp.exception.service.weatherapi.ApiRequestException;
import com.alexshin.weatherapp.model.dto.GeocodingApiResponseDTO;
import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.util.PropertiesUtil;
import com.alexshin.weatherapp.util.ProxyUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class WeatherService {
    private final String API_KEY;
    private static String protocol = "https";
    private static final String SERVICE_URI = "api.openweathermap.org";
    private static final String WEATHER_BY_NAME_REQUEST = "/data/2.5/weather?q=%s&appid=%s&units=metric";
    private static final String WEATHER_BY_COORDS_REQUEST = "/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric";
    private static final String GEOCODING_REQUEST = "/geo/1.0/direct?q=%s&limit=%d&appid=%s";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient client = createHttpClient();
    private static final int locNumLimit = Integer.parseInt(PropertiesUtil.getProperty("location.search.limit"));

    public WeatherService() {
        API_KEY = System.getenv("WEATHER_API_KEY");
        if (API_KEY == null) {
            throw new ApiKeyNotFoundException("OpenWeather API key not found in environment variables");
        }
    }

    // TODO: there might be a list of locations?
    public WeatherApiResponseDTO getWeatherByLocationName(String name) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(buildWeatherByNameRequestUrl(name))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), WeatherApiResponseDTO.class);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ApiRequestException("Exception in method 'getWeatherByLocationCoords': %s".formatted(e.getMessage()));
        }

    }


    public WeatherApiResponseDTO getWeatherByLocationCoords(BigDecimal latitude, BigDecimal longitude) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(buildWeatherByCoordsRequestUrl(latitude, longitude))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), WeatherApiResponseDTO.class);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ApiRequestException("Exception in method 'getWeatherByLocationCoords': %s".formatted(e.getMessage()));
        }


    }

    public List<GeocodingApiResponseDTO> searchLocationsByName(String name) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(buildGeocodingRequest(name))
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ApiRequestException("Exception in method 'getWeatherByLocationCoords': %s".formatted(e.getMessage()));
        }

    }


    private URI buildGeocodingRequest(String name) throws URISyntaxException {
        return new URI(protocol + "://" +
                SERVICE_URI +
                GEOCODING_REQUEST.formatted(name, locNumLimit, API_KEY));
    }

    private URI buildWeatherByCoordsRequestUrl(BigDecimal latitude, BigDecimal longitude) throws URISyntaxException {
        return new URI(protocol + "://" +
                SERVICE_URI +
                WEATHER_BY_COORDS_REQUEST.formatted(latitude, longitude, API_KEY));
    }


    private URI buildWeatherByNameRequestUrl(String name) throws URISyntaxException {
        return new URI(protocol + "://" +
                SERVICE_URI +
                WEATHER_BY_NAME_REQUEST.formatted(name, API_KEY));
    }

    private HttpClient createHttpClient() {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5L));

        if (ProxyUtil.isProxyPresent()) {
            ProxyUtil.setProxy(clientBuilder);
            protocol = "http";
        }

        return clientBuilder.build();
    }


}
