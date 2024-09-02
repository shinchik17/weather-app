package com.alexshin.weatherapp.filter;

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

@WebFilter(urlPatterns = {"/login", "/registration"}, filterName = "AuthFilter")
public class AuthFilter extends BaseFilter {
    private final Logger logger = LogManager.getLogger();


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        String method = req.getMethod();

        logger.info("%s -> Enter doFilter(), servletPath: %s, method: %s".formatted(getFilterName(), servletPath, method));

        Optional<String> optSessionId = CookieUtil.extractSessionCookie(req);

        if (optSessionId.isPresent()) {
            redirectToRootContext(resp);
            logger.info("%s -> Session presents, redirect to the rootContext".formatted(getFilterName()));
            return;
        }

        chain.doFilter(req, resp);
        logger.info("%s -> Process to next filter".formatted(getFilterName()));

    }

}
