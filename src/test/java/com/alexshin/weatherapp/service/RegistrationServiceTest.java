package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.exception.service.SuchUserExistsException;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.util.HibernateUtil;
import com.alexshin.weatherapp.util.MigrationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationServiceTest {

    UserService userService = UserService.getInstance();
    RegistrationService regService = RegistrationService.getInstance();

    String EXISTING_LOGIN = "test_username1";
    String NONEXISTENT_LOGIN = "test_username4";

    UserDTO newUser = UserDTO.builder().login(NONEXISTENT_LOGIN).password("pass").build();

    UserDTO existingUser = UserDTO.builder().login(EXISTING_LOGIN).password("pass").build();


    @BeforeEach
    void setup() {
        MigrationUtil.runFlywayMigration(HibernateUtil.getConfiguration());
    }

    @AfterEach
    void clean() {
        MigrationUtil.cleanDS(HibernateUtil.getConfiguration());
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
