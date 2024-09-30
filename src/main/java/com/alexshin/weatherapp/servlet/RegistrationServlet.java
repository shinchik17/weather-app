package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.service.RegistrationService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;
import static com.alexshin.weatherapp.util.ParsingUtil.parsePassword;


@WebServlet(urlPatterns = "/register")
public class RegistrationServlet extends AbstractAuthServlet {
    private final RegistrationService regService = RegistrationService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("registration", resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

            UserDTO user = new UserDTO(
                    parseLogin(req.getParameter("login")),
                    parsePassword(req.getParameter("password")),
                    parsePassword(req.getParameter("pass-repeat"))
            );

            regService.register(user);

            resp.sendRedirect("%s/login".formatted(rootPath));

        } catch (Exception e) {
            handleException(e, req);
        }
        processTemplate("registration", resp);

    }


}
