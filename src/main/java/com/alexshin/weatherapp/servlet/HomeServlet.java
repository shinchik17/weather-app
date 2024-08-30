package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.AuthenticationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "")
public class HomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            if (req.getAttribute("user") == null) {
                throw new AuthenticationException("User from request is null.");
            }
            processTemplate("home", req, resp);

        } catch (AuthenticationException e) {
            processTemplate("home-unauthorized", req, resp);
        }

    }
}
