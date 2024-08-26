package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@WebServlet(urlPatterns = "")
public class HomeServlet extends BaseServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String sessionId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("SessionId"))
                    .findAny()
                    .orElseThrow()
                    .getValue();

            // TODO: DTO's with mapper?
            User user = userService.findUserBySession(sessionId);
            req.setAttribute("username", user.getLogin());
            processTemplate("home", req, resp);

        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        } catch (NoSuchElementException e){

            processTemplate("home-unauthorized", req, resp);
//            String path = getServletContext().getContextPath() + "/home-unauthorized";
//            req.getRequestDispatcher(path).forward(req, resp);
        }

    }
}
