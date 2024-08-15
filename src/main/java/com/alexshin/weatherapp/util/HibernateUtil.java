package com.alexshin.weatherapp.util;

import com.alexshin.weatherapp.entity.Location;
import com.alexshin.weatherapp.entity.Session;
import com.alexshin.weatherapp.entity.User;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Map;
import java.util.Properties;

public final class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;
    private static final Configuration configuration;

    private HibernateUtil() {
        throw new UnsupportedOperationException();
    }

    static {

        try {
            configuration = new Configuration();
            setConfigurationProperties(PropertiesUtil.getAllProperties());
            sessionFactory = configuration.buildSessionFactory();
            runFlywayMigration();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setConfigurationProperties(Properties properties) {

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String propertyName = entry.getKey().toString();
            String propertyValue = entry.getValue().toString();

            if (propertyName.startsWith("hibernate.")) {
                configuration.setProperty(propertyName, propertyValue);
            } else if (propertyName.startsWith("ds.")) {
                configuration.setProperty(
                        "hibernate.connection." + propertyName.substring(3),
                        propertyValue);
            }
        }

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Location.class);
        configuration.addAnnotatedClass(Session.class);
    }


    private static void runFlywayMigration() {
        Flyway flyway = Flyway.configure()
                .dataSource(
                        configuration.getProperty("hibernate.connection.url"),
                        configuration.getProperty("hibernate.connection.username"),
                        configuration.getProperty("hibernate.connection.password")
                )
                .load();
        flyway.migrate();
    }


}

