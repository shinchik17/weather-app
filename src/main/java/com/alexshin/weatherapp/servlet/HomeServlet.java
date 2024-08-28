package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.exception.service.AuthenticationException;
import com.alexshin.weatherapp.service.AuthorizationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

@WebServlet(urlPatterns = "")
public class HomeServlet extends BaseServlet {
    private final AuthorizationService authService = AuthorizationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String sessionId = getSessionId(req);
            // TODO: DTO's with mapper?
            User user = authService.findUserBySession(sessionId);
            req.setAttribute("username", user.getLogin());
            processTemplate("home", req, resp);

        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        } catch (NoSuchElementException | AuthenticationException e){

            processTemplate("home-unauthorized", req, resp);
//            String path = getServletContext().getContextPath() + "/home-unauthorized";
//            req.getRequestDispatcher(path).forward(req, resp);
        }

    }
}
