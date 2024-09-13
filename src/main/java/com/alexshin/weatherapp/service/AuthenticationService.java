package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.exception.service.NoSuchUserSessionException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.UserSessionDTO;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import jakarta.persistence.NoResultException;
import org.modelmapper.ModelMapper;

import java.util.NoSuchElementException;

import static com.alexshin.weatherapp.util.EncryptionUtil.passwordMatches;

public class AuthenticationService {
    private static final AuthenticationService INSTANCE = new AuthenticationService();
    private final UserService userService = UserService.getInstance();
    private final UserSessionService userSessionService = UserSessionService.getInstance();
    private final ModelMapper mapper = new ModelMapper();

    private AuthenticationService() {
    }

    public static AuthenticationService getInstance() {
        return INSTANCE;
    }


    public UserSessionDTO logIn(UserDTO userDTO) {

        User user;
        UserSession userSession;
        try {
            user = userService.findByLogin(userDTO.getLogin()).orElseThrow();

            if (!passwordMatches(userDTO.getPassword(), user.getPassword())) {
                throw new IncorrectPasswordException("Incorrect password");
            }

        } catch (NoResultException | NoSuchElementException e) {
            throw new NoSuchUserException("No user with login '%s'.".formatted(userDTO.getLogin()));
        }

        try {
            userSession = userSessionService.findByUserLogin(user.getLogin());
        } catch (NoSuchUserSessionException e) {
            userSession = userSessionService.createSession(user);
        }

        return mapper.map(userSession, UserSessionDTO.class);
    }

    public void logOut(String login) {
        userSessionService.deleteByUserLogin(login);
    }


    public UserDTO findUserBySessionId(String sessionId) {

        try {
            userSessionService.updateUserSessionState(sessionId);
            User user = userService.findUserBySessionId(sessionId);
            return mapper.map(user, UserDTO.class);

        } catch (NoSuchUserException e) {
            throw new NoSuchUserException("No user found for given SessionId");
        } catch (NoSuchElementException e) {
            throw new NoSuchUserSessionException("No session found for given SessionId");
        }

    }


}
