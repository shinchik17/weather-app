package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.entity.User;
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
    private final AuthorizationService authService = AuthorizationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String locationName = parseLocationName(req.getParameter("location-name"));
            String sessionId = getSessionId(req);

            User user = authService.findUserBySession(sessionId);
            req.setAttribute("username", user.getLogin());

            // TODO: implement method
        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            String path = getServletContext().getContextPath() + "/home-unauthorized";
            req.getRequestDispatcher(path).forward(req, resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


}
