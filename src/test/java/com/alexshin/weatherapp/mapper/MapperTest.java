package com.alexshin.weatherapp.mapper;

import com.alexshin.weatherapp.model.Mapper;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

// TODO: mapper tests to DTO, npw failes on UserSession mapping
//  modelMapper doesn't work without setters
public class MapperTest {

    Mapper mapper = new Mapper();

    User user = User.builder()
            .id(1L)
            .login("login")
            .password("pass")
            .build();

    UserSession userSession = UserSession.builder()
            .id("id")
            .user(user)
            .expiresAt(LocalDateTime.now())
            .build();

    @Test
    void UserSession_entityToDto(){

        var session = mapper.toDto(userSession);
        Assertions.assertEquals(userSession.getId(), session.getId());
        Assertions.assertEquals(userSession.getExpiresAt(), session.getExpiresAt());

    }

    @Test
    void User_entityToDto(){

        var userDTO = mapper.toDto(user);
        Assertions.assertEquals(user.getId(), userDTO.getId());
        Assertions.assertEquals(user.getLogin(), userDTO.getLogin());
        Assertions.assertEquals(user.getPassword(), userDTO.getPassword());

    }

}
