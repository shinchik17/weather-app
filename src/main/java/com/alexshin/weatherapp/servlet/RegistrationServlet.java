package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.exception.service.AuthenticationException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.service.RegistrationService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;
import static com.alexshin.weatherapp.util.ParsingUtil.parsePassword;


@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends BaseServlet {
    private final RegistrationService regService = RegistrationService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("registration", req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

            // TODO: check login unique
            UserDTO user = new UserDTO(
                    parseLogin(req.getParameter("login")),
                    parsePassword(req.getParameter("password")),
                    parsePassword(req.getParameter("pass-repeat"))
            );

            // TODO: check for session id cookie
            regService.register(user);

            resp.sendRedirect("%s/login".formatted(rootPath));

        } catch (IllegalArgumentException e) {
            // TODO: handle exception
            throw new RuntimeException(e);
        } catch (AuthenticationException e) {
            // TODO: handle exception. JS tip?
        }

    }


}
