package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private RegistrationService regService = RegistrationService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("registration", req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO: check login unique, passwords match and other validation
        // TODO: implement session service, setting id etc
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String passRepeat = req.getParameter("pass-repeat");

        HttpSession session = req.getSession();
        String id = session.getId();

        regService.register(login, password);

        String path = getServletContext().getContextPath();
        resp.sendRedirect("%s/login".formatted(path));

    }


}
