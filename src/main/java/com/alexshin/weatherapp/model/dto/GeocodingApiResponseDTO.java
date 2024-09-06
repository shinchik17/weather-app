package com.alexshin.weatherapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingApiResponseDTO {

    private String name;

    private String country;

    private BigDecimal lon;

    private BigDecimal lat;


}
