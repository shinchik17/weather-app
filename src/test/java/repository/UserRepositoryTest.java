package repository;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.util.HibernateTestUtil;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserRepositoryTest {
    SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
    UserRepository userRepo = new UserRepository(sessionFactory);
    User transientUser;

    @BeforeEach
    void setup() {

        transientUser = User.builder()
                .login("test_login1")
                .password("test_pass1")
                .build();
//        User persistentUser = User.builder()
//                .login("test_login1")
//                .password("test_pass1")
//                .build();
    }

    @Test()
    void save() {
        userRepo.save(transientUser);
        Assertions.assertTrue(userRepo.findById(transientUser.getId()).isPresent());
    }

    @Test
    void find() {
        Assertions.assertTrue(userRepo.findById(transientUser.getId()).isPresent());
    }



}
