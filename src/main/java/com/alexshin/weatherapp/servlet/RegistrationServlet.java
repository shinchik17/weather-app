package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;
import static com.alexshin.weatherapp.util.ParsingUtil.parsePassword;


@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private final RegistrationService regService = RegistrationService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("registration", req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            // TODO: check login unique, passwords match and other validation
            // TODO: implement session service, setting id etc
            String login = parseLogin(req.getParameter("login"));
            String password = parsePassword(req.getParameter("password"));
            String passRepeat = parsePassword(req.getParameter("pass-repeat"));

            if (!password.equals(passRepeat)) {
                throw new IncorrectPasswordException("Passwords do not match.");
            }


            regService.register(login, password);

            String path = getServletContext().getContextPath();
            resp.sendRedirect("%s/login".formatted(path));

        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        }

    }


}
