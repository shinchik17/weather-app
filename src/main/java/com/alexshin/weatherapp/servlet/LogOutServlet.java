package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.service.AuthenticationService;
import com.alexshin.weatherapp.util.CookieUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.alexshin.weatherapp.util.ParsingUtil.parseLogin;

@WebServlet("/logout")
public class LogOutServlet extends AbstractAuthServlet {
    private final AuthenticationService authenticationService = AuthenticationService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {

            String login = parseLogin(req.getParameter("login"));
            authenticationService.logOut(login);
            CookieUtil.deleteSessionCookie(resp, rootPath);

        } catch (Exception e) {
            handleException(e, req);
        }

        redirectToRootContext(resp);

    }


}
