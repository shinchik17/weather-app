package com.alexshin.weatherapp.model;

import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.UserSessionDTO;
import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;

public class Mapper {
    private final ModelMapper modelMapper = new ModelMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public UserDTO toDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


    public User toEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public LocationDTO toDto(Location location) {
        return modelMapper.map(location, LocationDTO.class);
    }

    public Location toEntity(LocationDTO locationDTO){
        return modelMapper.map(locationDTO, Location.class);
    }

    public UserSessionDTO toDto(UserSession userSession){
        return modelMapper.map(userSession, UserSessionDTO.class);
    }

    public UserSession toEntity(UserSessionDTO userSessionDTO){
        return modelMapper.map(userSessionDTO, UserSession.class);
    }




}
