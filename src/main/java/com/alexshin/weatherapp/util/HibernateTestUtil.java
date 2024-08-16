package com.alexshin.weatherapp.util;


import com.alexshin.weatherapp.entity.Location;
import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.entity.UserSession;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;
import java.util.Properties;


public final class HibernateTestUtil {

    @Getter
    private static final SessionFactory sessionFactory;
    private static final Configuration configuration;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");


    static {
        try {
            // TODO: add logging jdbc url
            // TODO: remove duplicate code of buildConfiguration()
            postgres.start();
            configuration = buildConfiguration(PropertiesUtil.getAllProperties());
            configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
            configuration.setProperty("hibernate.connection.username", postgres.getUsername());
            configuration.setProperty("hibernate.connection.password", postgres.getPassword());
            sessionFactory = configuration.buildSessionFactory();

            if (Boolean.parseBoolean(PropertiesUtil.getProperty("use_flyway"))) {
                MigrationUtil.runFlywayMigration(configuration);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Configuration buildConfiguration(Properties properties) {
        Configuration cfg = new Configuration();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey().toString();
            String propertyValue = entry.getValue().toString();

            if (propertyName.startsWith("hibernate.")) {
                cfg.setProperty(propertyName, propertyValue);
            } else if (propertyName.startsWith("ds.")) {
                cfg.setProperty(
                        "hibernate.connection." + propertyName.substring(3),
                        propertyValue);
            }
        }

        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Location.class);
        cfg.addAnnotatedClass(UserSession.class);

        return cfg;
    }



}
