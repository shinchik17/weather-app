package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.AuthenticationException;
import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.service.LocationService;
import com.alexshin.weatherapp.service.WeatherService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "", name = "HomeServlet")
public class HomeServlet extends BaseServlet {
    private final LocationService locService = new LocationService();
    private final WeatherService weatherService = new WeatherService();
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
                    .map(
                            loc -> weatherService.getWeatherByLocationCoords(loc.getLatitude(), loc.getLongitude())
                    )
                    .toList();


            req.setAttribute("foundLocations", locsWithWeather);
            processTemplate("home", req, resp);

        } catch (AuthenticationException e) {
            processTemplate("home-unauthorized", req, resp);
        } catch (ClassCastException e){
            Object user = req.getAttribute("user");
        }

    }
}
