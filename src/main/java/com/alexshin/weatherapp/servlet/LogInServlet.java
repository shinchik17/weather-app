package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.UserSessionDTO;
import com.alexshin.weatherapp.service.AuthenticationService;
import com.alexshin.weatherapp.util.CookieUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;
import static com.alexshin.weatherapp.util.ParsingUtil.parsePassword;

@WebServlet("/login")
public class LogInServlet extends AbstractAuthServlet {
    private final AuthenticationService authenticationService = AuthenticationService.getInstance();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("login", resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserDTO userDTO = new UserDTO(
                    parseLogin(req.getParameter("login")),
                    parsePassword(req.getParameter("password"))
            );

            UserSessionDTO session = authenticationService.logIn(userDTO);
            CookieUtil.setSessionCookie(resp, session, rootPath);

            redirectToRootContext(resp);

        } catch (Exception e) {
            handleException(e, req);
        }

        processTemplate("login", resp);
    }


}
