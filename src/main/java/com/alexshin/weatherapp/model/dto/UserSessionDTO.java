package com.alexshin.weatherapp.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserSessionDTO {
    String id;
    UserDTO user;
    LocalDateTime expiresAt;
}
