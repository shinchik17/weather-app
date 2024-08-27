package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.entity.User;

import static com.alexshin.weatherapp.util.EncryptUtil.hashPassword;

public class RegistrationService {
    private static final RegistrationService INSTANCE = new RegistrationService();
    private final UserService userService = UserService.getInstance();

    private RegistrationService() {
    }

    public static RegistrationService getInstance() {
        return INSTANCE;
    }

    //TODO: dto
    public void register(String login, String password) {
        User user = User.builder()
                .login(login)
                .password(hashPassword(password))
                .build();
        userService.save(user);
    }


}
