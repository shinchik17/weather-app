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

@WebServlet("/logout")
public class LogOutServlet extends BaseServlet {
    private final AuthorizationService authorizationService = AuthorizationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            // TODO: test deleting 0 lines
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

//        processTemplate("login", req, resp);
    }



}
