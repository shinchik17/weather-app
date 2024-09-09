package com.alexshin.weatherapp.service;


import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.exception.service.NoSuchUserException;
import com.alexshin.weatherapp.exception.service.SuchUserExistsException;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.NoSuchElementException;
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
                throw new SuchUserExistsException("User with login %s is already registered.");
            }

            throw e;
        }
    }

    public User findUserBySessionId(String sessionId) {

        try {
            return userRepository.findBySessionId(sessionId).orElseThrow();
        } catch (NoResultException | NoSuchElementException e) {
            throw new NoSuchUserException("No user found for current SessionId");
        }

    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }


}
