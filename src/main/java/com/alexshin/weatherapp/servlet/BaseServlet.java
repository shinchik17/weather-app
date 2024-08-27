package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.IWebContext;

import java.io.IOException;
import java.util.Arrays;

public class BaseServlet extends HttpServlet {
    private ITemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.templateEngine = (ITemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        IWebContext webContext = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        templateEngine.process(templateName, webContext, resp.getWriter());
    }

    protected String getSessionId(HttpServletRequest req){
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("SessionId"))
                .findAny()
                .orElseThrow()
                .getValue();

    }


}


