package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.entity.UserSession;
import com.alexshin.weatherapp.service.AuthorizationService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;
import static com.alexshin.weatherapp.util.ParsingUtil.parsePassword;

@WebServlet("/login")
public class AuthorizationServlet extends BaseServlet {
    private final AuthorizationService authorizationService = AuthorizationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = parseLogin(req.getParameter("login"));
            String password = parsePassword(req.getParameter("password"));

            // TODO: create records dto?
            UserSession userSession = authorizationService.logIn(login, password);

            // TODO: mapper
            Cookie cookie = new Cookie("SessionId", userSession.getId());
            long age = ChronoUnit.HOURS.between(userSession.getExpiresAt(), LocalDateTime.now());
            cookie.setMaxAge((int) age);
            resp.addCookie(new Cookie("SessionId", userSession.getId()));

            String path = getServletContext().getContextPath() + "/";
            resp.sendRedirect(path);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String login = parseLogin(req.getParameter("login"));

            authorizationService.logOut(login);

            Cookie cookie = new Cookie("SessionId", "");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);

            String path = getServletContext().getContextPath() + "/";
            resp.sendRedirect(path);
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

    }

}
