package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import com.alexshin.weatherapp.repository.UserSessionRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.util.PropertiesUtil;

import java.time.LocalDateTime;
import java.util.Optional;
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
        Optional<UserSession> userSession = userSessionRepository.findById(id);
        if (userSession.isPresent() && !isValidSession(userSession.get())) {
            userSessionRepository.delete(id);
        }

    }


    public Optional<UserSession> findByUserLogin(String login) {
        return userSessionRepository.findByUserLogin(login);
    }


    private boolean isValidSession(UserSession userSession) {
        return userSession.getExpiresAt().isAfter(LocalDateTime.now());
    }


    private LocalDateTime getDefaultLifespan() {
        long hoursLifespan = Long.parseLong(PropertiesUtil.getProperty("session.hours_lifespan"));
        return LocalDateTime.now().plusHours(hoursLifespan);
    }


}
