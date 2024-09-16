package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.AuthenticationException;
import com.alexshin.weatherapp.exception.service.weatherapi.WeatherApiCallException;
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
import java.util.Objects;
import java.util.Optional;

@WebServlet(urlPatterns = "", name = "HomeServlet")
public class HomeServlet extends BaseServlet {
    private final LocationService locService = LocationService.getInstance();
    private final WeatherService weatherService = WeatherService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            if (req.getAttribute("user") == null) {
                throw new AuthenticationException("User from request is null.");
            }

            UserDTO user = (UserDTO) req.getAttribute("user");
            List<LocationDTO> locationsList = locService.findByUser(user);
            List<WeatherApiResponseDTO> locsWithWeather = weatherService.getWeatherForUserLocationsList(locationsList);

            if (locsWithWeather.isEmpty()) {
                req.setAttribute("message", "No locations added");
            } else {
                req.setAttribute("weatherCards", locsWithWeather);
            }
            processTemplate("home", req, resp);

        } catch (Exception e) {
            handleException(e, req, resp);
        }

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            if (req.getAttribute("user") == null) {
                throw new AuthenticationException("User from request is null.");
            }

            UserDTO user = (UserDTO) req.getAttribute("user");
            DeleteLocationRequestDTO deleteRequest = objectMapper.readValue(req.getReader(), DeleteLocationRequestDTO.class);
            Optional<LocationDTO> location = locService.findById(deleteRequest.getLocationId());

            if (location.isPresent()) {
                long locOwnerId = location.get().getUser().getId();
                if (Objects.equals(locOwnerId, user.getId())) {
                    locService.deleteById(deleteRequest.getLocationId());
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            resp.setContentType("application/json;utf-8");


        } catch (Exception e) {
            logger.warn("Caught %s when handling delete location request".formatted(e));
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }

    private void handleException(Exception e, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.warn(e);
        if (e.getCause() != null) {
            logger.warn("Caused by %s".formatted(e.getCause()));
        }

        String error;
        if (e instanceof WeatherApiCallException) {
            error = "Service error. Please try again later";
        } else if (e instanceof AuthenticationException) {
            processTemplate("home-unauthorized", req, resp);
            return;
        } else {
            error = "Unknown error";
        }

        req.setAttribute("message", error);
        processTemplate("home", req, resp);
    }

}
