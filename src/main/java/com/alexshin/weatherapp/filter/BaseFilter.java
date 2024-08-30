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
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

// TODO: check what about templates access
@WebFilter(urlPatterns = {"", "/logout", "/search-results"})
public class BaseFilter extends HttpFilter {
    private final AuthorizationService authService = AuthorizationService.getInstance();
    protected final Set<String> ALLOWED_PATH_SET = new HashSet<>(Set.of(
            "", "/", "/registration", "/login", "/logout", "/search-results"
    ));
    protected String rootContextPath;
    private Logger logger = LogManager.getLogger();

    @Override
    public void init() throws ServletException {
        super.init();
        rootContextPath = getServletContext().getContextPath();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;

        String path = req.getRequestURI().substring(rootContextPath.length());
        logger.info("BaseFilter -> Enter doFilter(), path: %s".formatted(path));

        Optional<String> optSessionId = CookieUtil.extractSessionCookie((HttpServletRequest) request);

        UserDTO user;
        try {
            String sessionId = optSessionId.orElseThrow();
            if (request.getAttribute("user") != null){
                logger.info("BaseFilter -> Process to next filter");
                chain.doFilter(request, response);
                return;
            }
            user = authService.findUserBySessionId(sessionId);
        } catch (AuthenticationException | NoSuchElementException e) {
            String rootPath = rootContextPath;
            ((HttpServletResponse) response).sendRedirect(rootPath);
            logger.info("BaseFilter -> redirect to %s".formatted(rootPath));
            return;
        }

        logger.info("BaseFilter -> Process to next filter");
        request.setAttribute("user", user);
        chain.doFilter(request, response);

    }
}
