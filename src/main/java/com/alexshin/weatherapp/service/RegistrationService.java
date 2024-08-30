package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.entity.User;

import static com.alexshin.weatherapp.util.EncryptionUtil.hashPassword;

public class RegistrationService {
    private static final RegistrationService INSTANCE = new RegistrationService();
    private final UserService userService = UserService.getInstance();

    private RegistrationService() {
    }

    public static RegistrationService getInstance() {
        return INSTANCE;
    }

    public void register(UserDTO user) {
        userService.save(
                User.builder()
                        .login(user.getLogin())
                        .password(hashPassword(user.getPassword()))
                        .build()
        );
    }


}
