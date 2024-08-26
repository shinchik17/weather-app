package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.entity.User;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserRepository extends BaseRepository<Long, User> {

    public UserRepository(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public Optional<User> findByLogin(String login) {
        return runWithinTxAndReturn(
                session -> {
                    String sqlString = "FROM User WHERE login = :login";
                    Query<User> query = session.createQuery(sqlString, clazz);
                    query.setParameter("login", login);
                    try {
                        return Optional.of(query.getSingleResult());
                    } catch (NoResultException e) {
                        return Optional.empty();
                    }
                }
        );

    }

    public Optional<User> findBySessionId(String userSessionId) {
        return runWithinTxAndReturn(
                session -> {
                    String sqlString = """
                            SELECT user FROM User user
                            JOIN UserSession userSession on user = userSession.user
                            WHERE userSession.id = :userSessionId
                            """;
                    Query<User> query = session.createQuery(sqlString, clazz);
                    query.setParameter("userSessionId", userSessionId);
                    try {
                        return Optional.of(query.getSingleResult());
                    } catch (NoResultException e) {
                        return Optional.empty();
                    }
                }
        );
    }

}
