package com.alexshin.weatherapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingApiResponseDTO {

    private String name;

    private String country;

    @JsonProperty("lon")
    private BigDecimal longitude;

    @JsonProperty("lat")
    private BigDecimal latitude;


}
