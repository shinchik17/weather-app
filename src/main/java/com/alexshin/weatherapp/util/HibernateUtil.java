package com.alexshin.weatherapp.util;

import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Map;
import java.util.Properties;

public final class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;
    private static final Configuration configuration;

    private HibernateUtil() {
        throw new UnsupportedOperationException("HibernateUtil cannot be instantiated");
    }

    static {
        try {
            configuration = buildConfiguration(PropertiesUtil.getAllProperties());
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

