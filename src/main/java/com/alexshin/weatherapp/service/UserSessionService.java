package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.exception.service.NoSuchUserSessionException;
import com.alexshin.weatherapp.exception.service.UserSessionExpiredException;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import com.alexshin.weatherapp.repository.UserSessionRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.util.PropertiesUtil;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

public class UserSessionService {
    private static final UserSessionService INSTANCE = new UserSessionService();
    private final UserSessionRepository userSessionRepository = new UserSessionRepository(HibernateUtil.getSessionFactory());

    private UserSessionService() {
    }


    public static UserSessionService getInstance() {
        return INSTANCE;
    }


    public UserSession createSession(User user) {
        UUID sessionId = UUID.randomUUID();

        UserSession userSession = UserSession.builder()
                .id(sessionId.toString())
                .user(user)
                .expiresAt(getDefaultLifespan())
                .build();

        userSessionRepository.save(userSession);

        return userSession;

    }


    public void deleteByUserLogin(String login) {
        userSessionRepository.deleteByUserLogin(login);
    }


    public void updateUserSessionState(String id) {
        UserSession userSession = userSessionRepository.findById(id).orElseThrow();

        if (!isValidSession(userSession)) {
            userSessionRepository.delete(id);
            throw new UserSessionExpiredException("Session with given id has expired");
        }

        userSession.setExpiresAt(getDefaultLifespan());
        userSessionRepository.update(userSession);


    }


    public UserSession findByUserLogin(String login) {

        try {
            return userSessionRepository.findByUserLogin(login).orElseThrow();
        } catch (NoResultException | NoSuchElementException e) {
            throw new NoSuchUserSessionException("No session found for given user found");
        }

    }


    private boolean isValidSession(UserSession userSession) {
        return userSession.getExpiresAt().isAfter(LocalDateTime.now());
    }


    private LocalDateTime getDefaultLifespan() {
        long hoursLifespan = Long.parseLong(PropertiesUtil.getProperty("session.hours_lifespan"));
        return LocalDateTime.now().plusHours(hoursLifespan);
    }


}
