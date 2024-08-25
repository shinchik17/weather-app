package com.alexshin.weatherapp.listener;

import com.alexshin.weatherapp.util.ThymeleafUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.thymeleaf.ITemplateEngine;

@WebListener
public class ServletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        ServletContextListener.super.contextInitialized(sce);
        ServletContext servletContext = sce.getServletContext();

        ITemplateEngine templateEngine = ThymeleafUtil.buildTemplateEngine(servletContext);


        servletContext.setAttribute("templateEngine", templateEngine);


    }
}
