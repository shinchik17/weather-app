package com.alexshin.weatherapp;

import com.alexshin.weatherapp.entity.Location;
import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.repository.BaseRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class Main {

    public static void main1(String[] args) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        User user = User.builder()
                .login("username@mail.ru")
                .password("password")
                .build();

        Location location = Location.builder()
                .name("kek")
                .latitude(BigDecimal.valueOf(43.2213))
                .longitude(BigDecimal.valueOf(-34.65))
                .user(user)
                .build();

        Transaction transaction = null;
        try (var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();


//            session.persist(user);
//            session.persist(location);

//            User user1 = session.get(User.class, 2L);
//            session.remove(user1);

            transaction.commit();
            System.out.println();


        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }

    }


    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        var repo = new BaseRepository<Long, User>(User.class, sessionFactory);


        var user = repo.findById(1L);
        

    }

}
