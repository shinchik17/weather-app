package com.alexshin.weatherapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// TODO: create error html-template and its support
@WebFilter(urlPatterns = "/")
public class BaseFilter extends HttpFilter {
    protected final Set<String> ALLOWED_URL_SET = new HashSet<>(Set.of(
            "", "/registration", "/login", "/logout", "/search-results"
    ));
    private final Logger logger = LogManager.getLogger();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        String method = req.getMethod();
        logger.info("BaseFilter -> Enter doFilter(), servletPath: %s, method: %s".formatted(servletPath, method));

        if (ALLOWED_URL_SET.contains(servletPath)) {
            chain.doFilter(req, resp);
            logger.info("BaseFilter -> Process to the next filter");
            return;
        }

        redirectToRootContext(resp);
        logger.info("BaseFilter -> Redirect to the rootContext");

    }


    protected void redirectToRootContext(HttpServletResponse resp) throws IOException {
        resp.sendRedirect(getServletContext().getContextPath());
    }
}
