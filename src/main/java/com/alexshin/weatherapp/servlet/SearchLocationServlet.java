package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.service.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLocationName;


@WebServlet(urlPatterns = "/search-results")
public class SearchLocationServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String locationName = parseLocationName(req.getParameter("location-name"));


            // TODO: implement OpenWeather request


        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO implement deleting location?
        super.doPost(req, resp);
    }


}
