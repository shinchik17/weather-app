package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.entity.UserSession;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;

public class UserSessionRepository extends BaseRepository<String, UserSession> {

    public UserSessionRepository(SessionFactory sessionFactory) {
        super(UserSession.class, sessionFactory);
    }

    public void deleteByUserLogin(String login) {

        runWithinTx(
                session -> {
                    String sqlString = """
                            DELETE FROM UserSession session
                            WHERE session.user in
                                (FROM User
                                WHERE login = :login)
                            """;
                    MutationQuery query = session.createMutationQuery(sqlString);
                    query.setParameter("login", login);
                    query.executeUpdate();
                }
        );
    }

}
