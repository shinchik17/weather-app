package com.alexshin.weatherapp.servlet;

import com.alexshin.weatherapp.util.ThymeleafUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;

import java.io.IOException;


@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private ITemplateEngine templateEngine;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.templateEngine = (ITemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processTemplate("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // TODO: check login unique, passwords match and other validation
        // TODO: implement session service, setting id etc
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String passRepeat = req.getParameter("pass-repeat");

        HttpSession session = req.getSession();
        String id = session.getId();

        super.doPost(req, resp);


    }

    protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        IWebContext webContext = ThymeleafUtil.buildWebContext(req, resp, getServletContext());
        templateEngine.process(templateName, webContext, resp.getWriter());

    }


}
