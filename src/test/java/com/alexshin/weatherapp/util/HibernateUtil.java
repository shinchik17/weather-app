package com.alexshin.weatherapp.util;


import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;
import java.util.Properties;


public final class HibernateUtil {

    @Getter
    private static final Configuration configuration;

    @Getter
    private static final SessionFactory sessionFactory;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    private HibernateUtil() {
        throw new UnsupportedOperationException("HibernateUtil cannot be instantiated");
    }


    static {
        try {
            postgres.start();
            configuration = buildConfiguration(PropertiesUtil.getAllProperties());
            sessionFactory = configuration.buildSessionFactory();

            if (Boolean.parseBoolean(PropertiesUtil.getProperty("flyway.use"))) {
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

        cfg.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        cfg.setProperty("hibernate.connection.username", postgres.getUsername());
        cfg.setProperty("hibernate.connection.password", postgres.getPassword());

        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Location.class);
        cfg.addAnnotatedClass(UserSession.class);

        return cfg;
    }

}
