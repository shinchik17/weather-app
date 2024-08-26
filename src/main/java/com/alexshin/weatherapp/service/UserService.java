package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.entity.UserSession;
import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.repository.UserSessionRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.util.PropertiesUtil;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.alexshin.weatherapp.util.EncryptUtil.passwordMatches;

public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final UserRepository userRepository = new UserRepository(HibernateUtil.getSessionFactory());
    private final UserSessionRepository userSessionRepository = new UserSessionRepository(HibernateUtil.getSessionFactory());

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }


    public User findUserBySession(String sessionId){

        try {
            return userRepository.findBySessionId(sessionId).orElseThrow();
        } catch (NoResultException | NoSuchElementException e) {
            throw new NoSuchUserException("No user found for current SessionId");
        }

    }


}
