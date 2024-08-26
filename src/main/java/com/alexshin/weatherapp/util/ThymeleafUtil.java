package com.alexshin.weatherapp.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public final class ThymeleafUtil {

    private ThymeleafUtil() {
        throw new UnsupportedOperationException("ThymeleafUtil cannot be instantiated");
    }


    public static ITemplateEngine buildTemplateEngine(ServletContext servletContext) {

        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);

        // Templates will be resolved as application (ServletContext) resources
        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);

        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");

        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs(3600000L);


        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;

    }


    public static IWebContext buildWebContext(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        IWebExchange webExchange = application.buildExchange(req, resp);

        return new WebContext(webExchange);
    }


}
