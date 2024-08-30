package com.alexshin.weatherapp.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter(urlPatterns = {"/login", "/registration"})
public class PublicUrlFilter extends BaseFilter {
    private Logger logger = LogManager.getLogger();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(rootContextPath.length());
        logger.info("PublicUrlFilter -> Enter doFilter(), path: %s".formatted(path));

        if (request.getAttribute("user") != null){
            String rootPath = rootContextPath;
            ((HttpServletResponse) response).sendRedirect(rootPath);
            logger.info("PublicUrlFilter -> redirect to %s".formatted(rootPath));
        }

        logger.info("PublicUrlFilter -> Process to next filter");
        chain.doFilter(request, response);

    }
}
