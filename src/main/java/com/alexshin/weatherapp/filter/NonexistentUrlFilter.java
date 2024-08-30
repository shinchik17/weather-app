package com.alexshin.weatherapp.filter;


import com.alexshin.weatherapp.exception.service.AuthenticationException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.service.AuthorizationService;
import com.alexshin.weatherapp.util.CookieUtil;
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
import java.util.NoSuchElementException;
import java.util.Optional;

// TODO: check what about templates access
@WebFilter(urlPatterns = "/")
public class NonexistentUrlFilter extends BaseFilter {
    private Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(rootContextPath.length());
        logger.info("NonexistentFilter -> Enter doFilter(), path: %s".formatted(path));

        if (!ALLOWED_PATH_SET.contains(path)) {
            String rootPath = rootContextPath;
            ((HttpServletResponse) response).sendRedirect(rootPath);
            logger.info("NonexistentFilter -> redirect to %s".formatted(rootPath));
            return;
        }

        logger.info("NonexistentFilter -> Process to next filter");
        chain.doFilter(request, response);

    }
}
