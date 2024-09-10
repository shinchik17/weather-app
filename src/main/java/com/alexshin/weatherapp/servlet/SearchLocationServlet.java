package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.SuchLocationExistsException;
import com.alexshin.weatherapp.exception.service.weatherapi.WeatherApiCallException;
import com.alexshin.weatherapp.model.dto.GeocodingApiResponseDTO;
import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.WeatherApiResponseDTO;
import com.alexshin.weatherapp.service.AuthorizationService;
import com.alexshin.weatherapp.service.LocationService;
import com.alexshin.weatherapp.service.WeatherService;
import com.alexshin.weatherapp.util.CookieUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.alexshin.weatherapp.util.ParsingUtil.parseCoord;
import static com.alexshin.weatherapp.util.ParsingUtil.parseLocationName;


@WebServlet(urlPatterns = "/search-results")
public class SearchLocationServlet extends BaseServlet {
    private final WeatherService weatherService = WeatherService.getInstance();
    private final AuthorizationService authService = AuthorizationService.getInstance();
    private final LocationService locService = LocationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String locationName = parseLocationName(req.getParameter("location-name"));

            List<GeocodingApiResponseDTO> locationsList = weatherService.searchLocationsByName(locationName);
            List<WeatherApiResponseDTO> locsWithWeather = weatherService.getWeatherForLocationsList(locationsList);

            req.setAttribute("foundLocations", locsWithWeather);
            processTemplate("search-results", req, resp);


        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        } catch (WeatherApiCallException e) {
            throw new RuntimeException(e);
        }


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

            resp.sendRedirect(rootPath);

            // TODO: handle exception
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (WeatherApiCallException e) {
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (SuchLocationExistsException e) {
            throw new RuntimeException(e);
        }

    }


}
