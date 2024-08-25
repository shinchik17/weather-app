package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.entity.UserSession;
import com.alexshin.weatherapp.service.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends BaseServlet {
    private final AuthorizationService authorizationService = AuthorizationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("/login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        // TODO: create records dto?
        UserSession userSession = authorizationService.logIn(login, password);


        resp.addCookie(new Cookie("SessionId", userSession.getId()));

        String path = getServletContext().getContextPath();
        resp.sendRedirect("%s/.".formatted(path));


    }

}
