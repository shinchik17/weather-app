package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.repository.LocationRepository;
import com.alexshin.weatherapp.service.LocationService;
import com.alexshin.weatherapp.service.WeatherService;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.IWebContext;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServletTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationService locService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private ITemplateEngine templateEngine;
    @Mock
    private IWebContext webContext;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ServletConfig servletConfig;

    @InjectMocks
    private HomeServlet homeServlet;


    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        System.out.println("sda");
//        homeServlet = new HomeServlet();

//        Field locationRepositoryField = locService.getClass().getDeclaredField("locationRepository");
//        locationRepositoryField.setAccessible(true);
//        locationRepositoryField.set(homeServlet, locationRepository);
//
//        Field locServiceField = homeServlet.getClass().getDeclaredField("locService");
//        locServiceField.setAccessible(true);
//        locServiceField.set(homeServlet, locService);
//
//
//        Field weatherServiceField = homeServlet.getClass().getDeclaredField("weatherService");
//        weatherServiceField.setAccessible(true);
//        weatherServiceField.set(homeServlet, weatherService);
//
//        Field templateEngineField = homeServlet.getClass().getDeclaredField("templateEngine");
//        templateEngineField.setAccessible(true);
//        templateEngineField.set(homeServlet, templateEngine);
//
//        Field webContextField = homeServlet.getClass().getDeclaredField("webContext");
//        webContextField.setAccessible(true);
//        webContextField.set(homeServlet, webContext);

    }

    @Test
    void doGet_ifNoUserInRequestAttribute_thenThrow() throws IOException {
        when(request.getAttribute("user")).thenReturn(null);
        verify(templateEngine, atMostOnce()).process("home-unauthorized", webContext, response.getWriter());

        Assertions.assertDoesNotThrow(() -> homeServlet.doGet(request, response));

    }


}