package com.alexshin.weatherapp.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    private HibernateUtil(){
        throw new UnsupportedOperationException();
    };

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

