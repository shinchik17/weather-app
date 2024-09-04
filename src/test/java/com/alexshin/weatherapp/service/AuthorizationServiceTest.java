package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.exception.service.NoSuchUserSessionException;
import com.alexshin.weatherapp.exception.service.UserSessionExpiredException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.repository.UserSessionRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class AuthorizationServiceTest {

    // TODO: реализовать все тесты с помощью рефлексии в BeforeEach, закоммитить, потом уже пробовать переходить
    //  на перезапуск контейнера в BeforeEach
    UserService userService = UserService.getInstance();
    UserSessionService userSessionService = UserSessionService.getInstance();
    AuthorizationService authService;

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
    void setup() throws NoSuchFieldException, IllegalAccessException {
        Field field = userService.getClass().getDeclaredField("userRepository");
        field.setAccessible(true);
        field.set(userService, new UserRepository(HibernateUtil.getSessionFactory()));

        Field field1 = userSessionService.getClass().getDeclaredField("userSessionRepository");
        field1.setAccessible(true);
        field1.set(userSessionService, new UserSessionRepository(HibernateUtil.getSessionFactory()));

        authService = AuthorizationService.getInstance();
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
    void logIn_whenSessionExpired_thenThrow(){
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
