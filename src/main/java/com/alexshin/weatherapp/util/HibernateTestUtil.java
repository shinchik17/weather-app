package com.alexshin.weatherapp.util;


import com.alexshin.weatherapp.entity.Location;
import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.entity.UserSession;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;
import java.util.Properties;



public final class HibernateTestUtil {

    private static final Configuration configuration;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");


    static {
        try {
            // TODO: add logging jdbc url
            // TODO: remove duplicate code of buildConfiguration()
            postgres.start();
            configuration = buildConfiguration(PropertiesUtil.getAllProperties());
//            Flyway.configure().cleanDisabled(false);

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

        cfg.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", postgres.getUsername());
        cfg.setProperty("hibernate.connection.password", postgres.getPassword());

        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Location.class);
        cfg.addAnnotatedClass(UserSession.class);

        return cfg;
    }


    public static SessionFactory buildSessionFactory() {
        try {
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            if (Boolean.parseBoolean(PropertiesUtil.getProperty("use_flyway"))) {
                MigrationUtil.cleanDS(configuration);
                MigrationUtil.runFlywayMigration(configuration);
            }
            return sessionFactory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
