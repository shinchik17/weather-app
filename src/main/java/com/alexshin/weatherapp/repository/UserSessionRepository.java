package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.model.entity.UserSession;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

import java.util.Optional;

public class UserSessionRepository extends BaseRepository<String, UserSession> {

    public UserSessionRepository(SessionFactory sessionFactory) {
        super(UserSession.class, sessionFactory);
    }

    public void deleteByUserLogin(String login) {

        runWithinTx(session -> {
            String sqlString = """
                    DELETE FROM UserSession s
                    WHERE s.user in
                        (FROM User
                        WHERE login = :login)
                    """;
            MutationQuery query = session.createMutationQuery(sqlString);
            query.setParameter("login", login);
            query.executeUpdate();
        });
    }


    public Optional<UserSession> findByUserLogin(String login) {

        return runWithinTxAndReturn(session -> {
            String sqlString = """
                    FROM UserSession s
                    WHERE s.user in
                        (FROM User
                        WHERE login = :login)
                    """;
            Query<UserSession> query = session.createQuery(sqlString, clazz);
            query.setParameter("login", login);
            try {
                return Optional.of(query.getSingleResult());
            } catch (NoResultException e) {
                return Optional.empty();
            }
        });
    }


}
