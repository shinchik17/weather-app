package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.entity.User;
import org.hibernate.SessionFactory;

public class UserRepository extends BaseRepository<Long, User> {

    public UserRepository(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }
}
