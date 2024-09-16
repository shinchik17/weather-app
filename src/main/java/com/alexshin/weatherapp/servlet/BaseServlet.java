package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.IWebContext;

import java.io.IOException;

public class BaseServlet extends HttpServlet {
    private ITemplateEngine templateEngine;
    protected String rootPath;
    protected final Logger logger = LogManager.getLogger();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.templateEngine = (ITemplateEngine) getServletContext().getAttribute("templateEngine");
        this.rootPath = getServletContext().getContextPath() + "/";
    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        IWebContext webContext = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        templateEngine.process(templateName, webContext, resp.getWriter());
    }

    protected void redirectToRootContext(HttpServletResponse resp) throws IOException {
        resp.sendRedirect(rootPath);
    }

}


