package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.dto.UserSessionDTO;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import org.modelmapper.ModelMapper;

import java.util.Optional;

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


        Optional<User> user = userService.findByLogin(userDTO.getLogin());
        if (user.isEmpty()) {
            throw new NoSuchUserException("No user with login '%s' registered".formatted(userDTO.getLogin()));
        }

        if (!passwordMatches(userDTO.getPassword(), user.get().getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        Optional<UserSession> userSession = userSessionService.findByUserLogin(userDTO.getLogin());

        if (userSession.isEmpty()) {
            userSession = Optional.of(userSessionService.createSession(user.get()));
        }

        return mapper.map(userSession, UserSessionDTO.class);
    }

    public void logOut(String login) {
        userSessionService.deleteByUserLogin(login);
    }


    public Optional<UserDTO> findUserBySessionId(String sessionId) {

        userSessionService.updateUserSessionState(sessionId);
        Optional<User> optUser = userService.findUserBySessionId(sessionId);

        return optUser.map(user -> mapper.map(user, UserDTO.class));
    }


}
