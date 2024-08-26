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

public class AuthorizationService {
    private static final AuthorizationService INSTANCE = new AuthorizationService();
    private final UserRepository userRepository = new UserRepository(HibernateUtil.getSessionFactory());
    private final UserSessionRepository userSessionRepository = new UserSessionRepository(HibernateUtil.getSessionFactory());

    private AuthorizationService() {
    }

    public static AuthorizationService getInstance() {
        return INSTANCE;
    }


    public UserSession logIn(String login, String password) {

        User user;
        try {
            user = userRepository.findByLogin(login).orElseThrow();
        } catch (NoResultException | NoSuchElementException e) {
            throw new NoSuchUserException("No user with login '%s'.".formatted(login));
        }

        if (!passwordMatches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        return createSession(user);
    }


    // TODO: extract to UserSessionService?
    public UserSession createSession(User user) {
        UUID sessionId = UUID.randomUUID();
        long hoursLifespan = Long.parseLong(PropertiesUtil.getProperty("session.hours_lifespan"));
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(hoursLifespan);

        UserSession userSession = UserSession.builder()
                .id(sessionId.toString())
                .user(user)
                .expiresAt(expiresAt)
                .build();


        userSessionRepository.save(userSession);

        return userSession;

    }

    public void logOut(String login){
        userSessionRepository.deleteByUserLogin(login);
    }

}
