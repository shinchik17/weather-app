package com.alexshin.weatherapp.model.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public final class LocationDTO {
    Long id;
    String name;
    UserDTO user;
    BigDecimal latitude;
    BigDecimal longitude;

}