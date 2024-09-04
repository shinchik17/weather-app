package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.exception.service.SuchUserExistsException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class RegistrationServiceTest {

    UserService userService = UserService.getInstance();
    RegistrationService regService;

    String EXISTING_LOGIN = "test_username1";
    String NONEXISTENT_LOGIN = "test_username4";

    UserDTO newUser = UserDTO.builder()
            .login(NONEXISTENT_LOGIN)
            .password("pass")
            .build();

    UserDTO existingUser = UserDTO.builder()
            .login(EXISTING_LOGIN)
            .password("pass")
            .build();


    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
//        Constructor<UserService> constructor = UserService.class.getDeclaredConstructor();
//        userService = constructor.newInstance()
        Field field = userService.getClass().getDeclaredField("userRepository");
        field.setAccessible(true);
        field.set(userService, new UserRepository(HibernateUtil.getSessionFactory()));
        regService = RegistrationService.getInstance();
    }


    @Test
    void register_thenCreateUser() {
        regService.register(newUser);
        Assertions.assertTrue(userService.findByLogin(newUser.getLogin()).isPresent());
    }

    @Test
    void register_ifLoginExists_thenThrow() {
        Assertions.assertThrows(SuchUserExistsException.class, () -> regService.register(existingUser));
    }


}
