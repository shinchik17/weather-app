package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.entity.UserSession;
import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.exception.service.NoSuchUserSessionException;
import jakarta.persistence.NoResultException;

import java.util.NoSuchElementException;

import static com.alexshin.weatherapp.util.EncryptUtil.passwordMatches;

public class AuthorizationService {
    private static final AuthorizationService INSTANCE = new AuthorizationService();
    private final UserService userService = UserService.getInstance();
    private final UserSessionService userSessionService = UserSessionService.getInstance();

    private AuthorizationService() {
    }

    public static AuthorizationService getInstance() {
        return INSTANCE;
    }


    public UserSession logIn(String login, String password) {

        User user;
        try {
            user = userService.findByLogin(login).orElseThrow();
        } catch (NoResultException | NoSuchElementException e) {
            throw new NoSuchUserException("No user with login '%s'.".formatted(login));
        }

        if (!passwordMatches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        return userSessionService.createSession(user);
    }

    public void logOut(String login) {
        userSessionService.deleteByUserLogin(login);
    }

    public User findUserBySession(String sessionId) {

        try {
            userSessionService.updateUserSessionState(sessionId);

            return userService.findUserBySession(sessionId);

        } catch (NoSuchUserException e) {
            throw new NoSuchUserException("No user found for given SessionId");
        } catch (NoSuchElementException e) {
            throw new NoSuchUserSessionException("No session found for given SessionId");
        }

    }

}
