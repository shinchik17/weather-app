package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.exception.service.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;

public abstract class AuthServlet extends BaseServlet {

    protected void handleException(Exception e, HttpServletRequest req){
        logger.warn(e);
        if (e.getCause() != null) {
            logger.warn("Caused by %s".formatted(e.getCause()));
        }

        String error;
        if (e instanceof IllegalArgumentException) {
            error = e.getMessage();
        } else if (e instanceof AuthenticationException) {
            error = e.getMessage();
        } else if (e instanceof BaseRepositoryException) {
            error = "Database error";
        } else {
            error = "Unknown error";
        }

        req.setAttribute("message", error);

    }

}


