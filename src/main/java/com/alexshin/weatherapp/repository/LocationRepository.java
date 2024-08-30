package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.model.entity.Location;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class LocationRepository extends BaseRepository<Long, Location> {

    public LocationRepository(SessionFactory sessionFactory) {
        super(Location.class, sessionFactory);
    }

    public List<Location> findByUserId(Long userId) {
        return runWithinTxAndReturn(
                session -> {
                    String sqlString = "FROM Location l WHERE l.user.id = :userId";
                    Query<Location> query = session.createQuery(sqlString, clazz);
                    query.setParameter("userId", userId);
                    return query.getResultList();
                }
        );
    }

}
