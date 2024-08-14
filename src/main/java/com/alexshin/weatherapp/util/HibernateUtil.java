package com.alexshin.weatherapp.util;

import com.alexshin.weatherapp.entity.Location;
import com.alexshin.weatherapp.entity.Session;
import com.alexshin.weatherapp.entity.User;
import lombok.Getter;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;
    private static final Configuration configuration;

    private HibernateUtil() {
        throw new UnsupportedOperationException();
    }

    ;

    static {
        try {
            configuration = new Configuration();
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Location.class);
            configuration.addAnnotatedClass(Session.class);

            // TODO: try to come back to one custom application.properties file

//            for (Map.Entry<Object, Object> entry : PropertiesUtil.getAllProperties().entrySet()) {
//                String propertyName = entry.getKey().toString();
//                if (!propertyName.startsWith("hibernate.")) {
//                    String propertyValue = entry.getValue().toString();
//                    configuration.setProperty(propertyName, propertyValue);
//                }
//            }

//            PropertiesUtil.getAllProperties().entrySet().stream()
//                    .filter(entry -> entry.getKey().toString().startsWith("hibernate."))
//                    .forEach(
//                            entry -> configuration.setProperty(
//                                    entry.getKey().toString(),
//                                    entry.getValue().toString()
//                            )
//                    );


//            configuration.configure("hibernate.properties");
            sessionFactory = configuration.buildSessionFactory();

            runFlywayMigration();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

