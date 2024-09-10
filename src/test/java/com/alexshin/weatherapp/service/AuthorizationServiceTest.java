package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.exception.service.NoSuchUserSessionException;
import com.alexshin.weatherapp.exception.service.UserSessionExpiredException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.util.MigrationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorizationServiceTest {

    UserSessionService userSessionService = UserSessionService.getInstance();
    AuthorizationService authService = AuthorizationService.getInstance();

    String EXPIRED_SESSION_ID = "test_session_id2";

    UserDTO newUser = UserDTO.builder()
            .login("test_username4")
            .password("pass")
            .build();

    UserDTO existingUser = UserDTO.builder()
            .login("test_username1")
            .password("test_pass1")
            .build();

    UserDTO existingUserWithoutSession = UserDTO.builder()
            .login("test_username3")
            .password("test_pass3")
            .build();

    @BeforeEach
    void setup() {
        MigrationUtil.runFlywayMigration(HibernateUtil.getConfiguration());
    }

    @AfterEach
    void clean() {
        MigrationUtil.cleanDS(HibernateUtil.getConfiguration());
    }


    @Test
    void logIn() {
        authService.logIn(existingUser);
        authService.logIn(existingUserWithoutSession);
        Assertions.assertDoesNotThrow(() -> userSessionService.findByUserLogin(existingUser.getLogin()));
        Assertions.assertDoesNotThrow(() -> userSessionService.findByUserLogin(existingUserWithoutSession.getLogin()));
    }

    @Test
    void logIn_ifUserIsUnregistered_thenThrow() {
        Assertions.assertThrows(NoSuchUserException.class, () -> authService.logIn(newUser));
    }

    @Test
    void logIn_whenSessionExpired_thenThrow() {
        Assertions.assertThrows(UserSessionExpiredException.class, () -> authService.findUserBySessionId(EXPIRED_SESSION_ID));
    }

    @Test
    void logOut_ifSessionExists() {
        authService.logOut(existingUser.getLogin());
        Assertions.assertThrows(NoSuchUserSessionException.class,
                () -> userSessionService.findByUserLogin(existingUser.getLogin()));
    }

    @Test
    void logOut_ifSessionDoesNotExist_thenThrow() {
        authService.logOut(existingUserWithoutSession.getLogin());
        Assertions.assertThrows(NoSuchUserSessionException.class,
                () -> userSessionService.findByUserLogin(existingUserWithoutSession.getLogin()));
    }


}
