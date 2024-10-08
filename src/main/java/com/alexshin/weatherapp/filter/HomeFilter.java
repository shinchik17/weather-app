package com.alexshin.weatherapp.filter;

import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.service.AuthenticationService;
import com.alexshin.weatherapp.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

@WebFilter(servletNames = "HomeServlet", filterName = "HomeFilter")
public class HomeFilter extends ProtectedUrlFilter {
    private final Logger logger = LogManager.getLogger();
    private final AuthenticationService authService = AuthenticationService.getInstance();

    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        String method = req.getMethod();
        logger.info("%s -> Enter doFilter(), servletPath: %s, method: %s".formatted(getFilterName(), servletPath, method));

        Optional<String> optSessionId = CookieUtil.extractSessionCookie(req);

        if (optSessionId.isEmpty()) {
            logger.info("%s -> Session is empty".formatted(getFilterName()));

        } else {
            Optional<Object> optUser = Optional.ofNullable(req.getAttribute("user"));

            if (optUser.isEmpty()) {
                logger.info("%s -> Session presents, but user is empty".formatted(getFilterName()));
                Optional<UserDTO> user = authService.findUserBySessionId(optSessionId.get());

                if (user.isPresent()) {
                    req.setAttribute("user", user.get());
                    logger.info("%s -> Setting user attribute".formatted(getFilterName()));
                } else {
                    CookieUtil.deleteSessionCookie(resp, rootPath);
                    logger.info("%s -> Deleting cookie".formatted(getFilterName()));
                }

            }
        }

        chain.doFilter(req, resp);
        logger.info("%s -> Process to next filter".formatted(getFilterName()));
    }

}
