package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.mindrot.jbcrypt.BCrypt;

public class RegistrationService {
    private static final RegistrationService INSTANCE = new RegistrationService();

    private final UserRepository userRepository = new UserRepository(HibernateUtil.getSessionFactory());

    private RegistrationService() {
    }

    public static RegistrationService getInstance() {
        return INSTANCE;
    }


    public void register(String login, String password) {
        User user = User.builder()
                .login(login)
                .password(encryptPassword(password))
                .build();
        userRepository.save(user);
    }


    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


}
