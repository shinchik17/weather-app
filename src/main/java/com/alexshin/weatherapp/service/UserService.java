package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.exception.service.SuchUserExistsException;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final UserRepository userRepository = new UserRepository(HibernateUtil.getSessionFactory());

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }


    public void save(User user) {
        try {
            userRepository.save(user);
        } catch (BaseRepositoryException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new SuchUserExistsException(
                        "User with login %s is already registered".formatted(user.getLogin())
                );
            }

            throw e;
        }
    }

    public Optional<User> findUserBySessionId(String sessionId) {
        return userRepository.findBySessionId(sessionId);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


}
