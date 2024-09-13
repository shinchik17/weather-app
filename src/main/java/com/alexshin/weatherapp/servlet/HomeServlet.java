package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.AuthenticationException;
import com.alexshin.weatherapp.exception.service.weatherapi.WeatherApiCallException;
import com.alexshin.weatherapp.exception.servlet.NoLocationsFoundException;
import com.alexshin.weatherapp.model.dto.DeleteLocationRequestDTO;
import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.service.LocationService;
import com.alexshin.weatherapp.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "", name = "HomeServlet")
public class HomeServlet extends BaseServlet {
    private final LocationService locService = LocationService.getInstance();
    private final WeatherService weatherService = WeatherService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // TODO implement deleting location?
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            if (req.getAttribute("user") == null) {
                throw new AuthenticationException("User from request is null.");
            }

            // TODO: check casting?
            UserDTO user = (UserDTO) req.getAttribute("user");
            List<LocationDTO> locationsList = locService.findByUser(user);

            List<WeatherApiResponseDTO> locsWithWeather = locationsList.stream()
                    .map(loc -> {
                        var locWithWeather = weatherService.getWeatherByLocationName(loc.getName());
                        locWithWeather.setLocationId(loc.getId());
                        return locWithWeather;
                    })
                    .toList();

            if (locsWithWeather.isEmpty()) {
                throw new NoLocationsFoundException();
            } else {
                req.setAttribute("foundLocations", locsWithWeather);
            }
            processTemplate("home", req, resp);

        } catch (Exception e) {
            String error;
            if (e instanceof AuthenticationException) {
                processTemplate("home-unauthorized", req, resp);
                return;
            } else if (e instanceof NoLocationsFoundException) {
                error = "No locations found";
            } else if (e instanceof WeatherApiCallException) {
                error = "Service error. Please try again later";
            } else {
                error = "Unknown error";
            }
            req.setAttribute("error", error);
            processTemplate("home", req, resp);
        }

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

        // TODO: parse, validate
        try {

            var deleteRequest = objectMapper.readValue(req.getReader(), DeleteLocationRequestDTO.class);
            locService.deleteById(deleteRequest.getLocationId());
            resp.setContentType("application/json;utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);

            // TODO: check exceptions, add correct status code
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }
}
