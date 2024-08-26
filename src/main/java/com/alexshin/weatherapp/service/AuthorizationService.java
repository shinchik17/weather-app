package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.entity.UserSession;
import com.alexshin.weatherapp.exception.service.IncorrectPasswordException;
import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.repository.UserSessionRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.util.PropertiesUtil;

import java.time.LocalDateTime;
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

        // TODO: handle NoResult
        Optional<User> optUser = userRepository.findByLogin(login);

        if (optUser.isEmpty()) {
            throw new NoSuchUserException("No user with login '%s'.".formatted(login));
        }

        User user = optUser.get();

        if (passwordMatches(password, user.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }

        return createSession(user);
    }


    // TODO: extract to UserSessionService?
    public UserSession createSession(User user) {
        UUID sessionId = UUID.randomUUID();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(
                Long.parseLong(PropertiesUtil.getProperty("lifespan_hours"))
        );

        UserSession userSession = UserSession.builder()
                .id(sessionId.toString())
                .user(user)
                .expiresAt(expiresAt)
                .build();


        userSessionRepository.save(userSession);

        return userSession;

    }

}
