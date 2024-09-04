package com.alexshin.weatherapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResponseDTO {

    @JsonProperty("name")
    private String cityName;

    @JsonProperty("id")
    private long cityId;

    private Coord coord;

    @JsonProperty("weather")
    private List<Weather> weathersList;

    private Main main;

    private Wind wind;

    private int visibility;

    private Clouds clouds;


    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        BigDecimal lon;
        BigDecimal lat;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private int temp;
        private int feelsLike;
        private int tempMin;
        private int tempMax;
        private int pressure;
        private int humidity;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        int speed;
        int deg;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        int all;
    }



}
