package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.exception.service.SuchUserExistsException;
import com.alexshin.weatherapp.service.AuthorizationService;
import com.alexshin.weatherapp.service.RegistrationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;
import static com.alexshin.weatherapp.util.ParsingUtil.parsePassword;


@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private final RegistrationService regService = RegistrationService.getInstance();
    private final AuthorizationService authService = AuthorizationService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // TODO: replace getSession id throwing with optional.isEmpty()?
            String sessionId = getSessionId(req);
            String path = getServletContext().getContextPath();
            resp.sendRedirect("%s/".formatted(path));
        } catch (NoSuchElementException e) {
            processTemplate("registration", req, resp);
        }


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

            // TODO: check for session id cookie
            regService.register(login, password);

            String path = getServletContext().getContextPath();
            resp.sendRedirect("%s/login".formatted(path));

        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        } catch (SuchUserExistsException e) {
            // TODO: handle exception. JS tip?
        }

    }


}
