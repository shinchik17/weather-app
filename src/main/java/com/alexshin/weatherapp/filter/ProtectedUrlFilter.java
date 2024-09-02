package com.alexshin.weatherapp.filter;

import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.service.AuthorizationService;
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

@WebFilter(urlPatterns = {"/search-results", "/logout"}, filterName = "ProtectedUrlFilter")
public class ProtectedUrlFilter extends BaseFilter {
    private final Logger logger = LogManager.getLogger();
    private final AuthorizationService authService = AuthorizationService.getInstance();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        String method = req.getMethod();
        logger.info("%s -> Enter doFilter(), servletPath: %s, method: %s".formatted(getFilterName(), servletPath, method));

        Optional<String> optSessionId = CookieUtil.extractSessionCookie(req);

        if (optSessionId.isEmpty()) {
            redirectToRootContext(resp);
            logger.info("%s -> Session is empty, redirect to the rootContext".formatted(getFilterName()));
            return;
        }

        Optional<Object> optUser = Optional.ofNullable(req.getAttribute("user"));
        if (optUser.isEmpty()) {
            UserDTO user = authService.findUserBySessionId(optSessionId.get());
            req.setAttribute("user", user);
        }

        chain.doFilter(req, resp);
        logger.info("%s -> Process to next filter".formatted(getFilterName()));
    }
}
