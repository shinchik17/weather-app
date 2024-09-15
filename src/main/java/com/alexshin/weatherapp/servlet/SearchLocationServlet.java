package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.weatherapi.WeatherApiCallException;
import com.alexshin.weatherapp.exception.servlet.NoLocationsFoundException;
import com.alexshin.weatherapp.model.dto.GeocodingApiResponseDTO;
import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.service.AuthenticationService;
import com.alexshin.weatherapp.service.LocationService;
import com.alexshin.weatherapp.service.WeatherService;
import com.alexshin.weatherapp.util.CookieUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.alexshin.weatherapp.util.ParsingUtil.parseCoord;
import static com.alexshin.weatherapp.util.ParsingUtil.parseLocationName;


@WebServlet(urlPatterns = "/search-results")
public class SearchLocationServlet extends BaseServlet {
    private final WeatherService weatherService = WeatherService.getInstance();
    private final AuthenticationService authService = AuthenticationService.getInstance();
    private final LocationService locService = LocationService.getInstance();
    private final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String locationName = parseLocationName(req.getParameter("location-name"));

            List<GeocodingApiResponseDTO> locationsList = weatherService.searchLocationsByName(locationName);
            List<WeatherApiResponseDTO> locsWithWeather = weatherService.getWeatherForFoundLocationsList(locationsList);

            if (locsWithWeather.isEmpty()){
                throw new NoLocationsFoundException();
            } else {
                req.setAttribute("weatherCards", locsWithWeather);
            }

        } catch (Exception e) {
            logger.error(e);
            String error;
            if (e instanceof IllegalArgumentException) {
                error = "Invalid location name format";
            } else if (e instanceof WeatherApiCallException) {
                error = "Service error. Please try again later";
            } else if (e instanceof NoLocationsFoundException) {
                error = "No locations found";
            } else {
                error = "Unknown error";
            }
            req.setAttribute("error", error);
        }

        processTemplate("search-results", req, resp);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

            String locationName = parseLocationName(req.getParameter("selected-loc-name"));
            BigDecimal locationLatitude = parseCoord(req.getParameter("selected-loc-lat"));
            BigDecimal locationLongitude = parseCoord(req.getParameter("selected-loc-lon"));

            UserDTO user = authService.findUserBySessionId(CookieUtil.extractSessionCookie(req).orElseThrow(AuthenticationException::new));

            LocationDTO location = LocationDTO.builder()
                    .name(locationName)
                    .latitude(locationLatitude)
                    .longitude(locationLongitude)
                    .user(user)
                    .build();

            locService.saveLocation(location);

        } catch (Exception e) {
            String error;
            if (e instanceof IllegalArgumentException) {
                error = "Invalid location attributes format";
            } else if (e instanceof WeatherApiCallException) {
                error = "Service error. Please try again later";
            } else {
                error = "Unknown error";
            }

            req.setAttribute("error", error);
        }

        resp.sendRedirect(rootPath);

    }


}
