package com.alexshin.weatherapp;


import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.util.EncryptionUtil;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

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
            session.getTransaction();
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

//        var reg = RegistrationService.getInstance();
//        var auth = AuthorizationService.getInstance();
//
        String pass1 = "pass1";



        String hash1 =  EncryptionUtil.hashPassword(pass1);
        String hash2 =  EncryptionUtil.hashPassword(pass1);
        String salt1 = BCrypt.gensalt();
        String salt2 = BCrypt.gensalt(10);
//
//        System.out.println(hash1.equals(hash2));
//        var a = LocalDateTime.parse("2001-09-11T00:00");
        System.out.println(salt1);
        System.out.println(salt2);
        System.out.println(System.getenv("BCRYPT_SALT"));
        System.out.println(System.getenv("aHTTP_PROXY"));

    }

}
