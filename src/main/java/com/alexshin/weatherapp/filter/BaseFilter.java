package com.alexshin.weatherapp.filter;

//import jakarta.servlet.Filter

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;

import java.io.IOException;


public class BaseFilter extends HttpFilter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

//        ServletContext servletContext = request.getServletContext();
//        if (request.getServletContext().getContextPath() == "/") {
//            RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/registration");
//            // TODO: change redirecting from registration to home page
//            requestDispatcher.forward(request, response);
//        }


    }
}
