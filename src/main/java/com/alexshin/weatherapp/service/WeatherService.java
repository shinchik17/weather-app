package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.exception.service.weatherapi.ApiKeyNotFoundException;
import com.alexshin.weatherapp.exception.service.weatherapi.WeatherApiCallException;
import com.alexshin.weatherapp.model.dto.GeocodingApiResponseDTO;
import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.util.PropertiesUtil;
import com.alexshin.weatherapp.util.ProxyUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class WeatherService {
    private static final WeatherService INSTANCE = new WeatherService();
    private static final String SERVICE_URI = "api.openweathermap.org";
    private static final String WEATHER_BY_NAME_REQUEST = "/data/2.5/weather?q=%s&appid=%s&units=metric";
    private static final String WEATHER_BY_COORDS_REQUEST = "/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric";
    private static final String GEOCODING_REQUEST = "/geo/1.0/direct?q=%s&limit=%d&appid=%s";
    private static final int locNumLimit = Integer.parseInt(PropertiesUtil.getProperty("location.search.limit"));
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String API_KEY;
    private final String protocol;
    private HttpClient client;

    private WeatherService() {
        API_KEY = System.getenv("WEATHER_API_KEY");
        if (API_KEY == null) {
            throw new ApiKeyNotFoundException("OpenWeather API key not found in environment variables");
        }

        if (ProxyUtil.isProxyPresent()){
            protocol = "http";
        } else {
            protocol ="https";
        }
        client = createHttpClient();
    }

    public static WeatherService getInstance(){
        return INSTANCE;
    }


    public List<GeocodingApiResponseDTO> searchLocationsByName(String name) {

        try {
            HttpRequest request = buildGeocodingRequest(name);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != SC_OK) {
                throw new WeatherApiCallException("Response status code %d".formatted(response.statusCode()));
            }

            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });

        } catch (Exception e) {
            throw new WeatherApiCallException("Exception in method 'searchLocationsByName': %s".formatted(e.getMessage()));
        }

    }


    public WeatherApiResponseDTO getWeatherByLocationName(String name) {

        try {
            HttpRequest request = buildWeatherByNameRequest(name);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != SC_OK) {
                throw new WeatherApiCallException("Response status code %d".formatted(response.statusCode()));
            }

            return objectMapper.readValue(response.body(), WeatherApiResponseDTO.class);

        } catch (Exception e) {
            throw new WeatherApiCallException("Exception in method 'getWeatherByLocationName': %s".formatted(e.getMessage()));
        }

    }


    public WeatherApiResponseDTO getWeatherByLocationCoords(BigDecimal latitude, BigDecimal longitude) {
        try {
            HttpRequest request = buildWeatherByCoordsRequest(latitude, longitude);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != SC_OK) {
                throw new WeatherApiCallException("Response status code %d".formatted(response.statusCode()));
            }
            return objectMapper.readValue(response.body(), WeatherApiResponseDTO.class);

        } catch (Exception e) {
            throw new WeatherApiCallException("Exception in method 'getWeatherByLocationCoords': %s".formatted(e.getMessage()));
        }

    }


    public List<WeatherApiResponseDTO> getWeatherForFoundLocationsList(List<GeocodingApiResponseDTO> locationsList) {
        return locationsList.stream()
                .map(
                        loc -> {
                            WeatherApiResponseDTO locWithWeather = getWeatherByLocationCoords(loc.getLatitude(), loc.getLongitude());
                            locWithWeather.setCityName(loc.getName());
                            return locWithWeather;
                        }
                )
                .toList();
    }

    public List<WeatherApiResponseDTO> getWeatherForUserLocationsList(List<LocationDTO> locationsList) {
        return locationsList.stream()
                .map(
                        loc -> {
                            WeatherApiResponseDTO locWithWeather = getWeatherByLocationCoords(loc.getLatitude(), loc.getLongitude());
                            locWithWeather.setLocationId(loc.getId());
                            locWithWeather.setCityName(loc.getName());
                            return locWithWeather;
                        }
                )
                .toList();
    }

    private HttpRequest buildGeocodingRequest(String name) throws URISyntaxException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        URI uri = new URI(protocol + "://" +
                SERVICE_URI +
                GEOCODING_REQUEST.formatted(encodedName, locNumLimit, API_KEY));

        return HttpRequest.newBuilder()
                .uri(uri)
                .build();
    }

    private HttpRequest buildWeatherByCoordsRequest(BigDecimal latitude, BigDecimal longitude) throws URISyntaxException {
        URI uri = new URI(protocol + "://" +
                SERVICE_URI +
                WEATHER_BY_COORDS_REQUEST.formatted(latitude, longitude, API_KEY));

        return HttpRequest.newBuilder()
                .uri(uri)
                .build();
    }

    private HttpRequest buildWeatherByNameRequest(String name) throws URISyntaxException {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        URI uri = new URI(protocol + "://" +
                SERVICE_URI +
                WEATHER_BY_NAME_REQUEST.formatted(encodedName, API_KEY));

        return HttpRequest.newBuilder()
                .uri(uri)
                .build();
    }

    private HttpClient createHttpClient() {
        HttpClient.Builder clientBuilder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5L));

        if (ProxyUtil.isProxyPresent()) {
            ProxyUtil.setProxy(clientBuilder);
        }

        return clientBuilder.build();
    }


}
