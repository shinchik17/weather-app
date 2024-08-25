package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.entity.UserSession;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserSessionRepository extends BaseRepository<String, UserSession> {

    public UserSessionRepository(SessionFactory sessionFactory) {
        super(UserSession.class, sessionFactory);
    }

}
