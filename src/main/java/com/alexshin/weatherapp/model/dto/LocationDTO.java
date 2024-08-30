package com.alexshin.weatherapp.model.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class LocationDTO {
    Long id;
    String name;
    UserDTO user;
    BigDecimal latitude;
    BigDecimal longitude;

    public LocationDTO(String name, UserDTO user, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}