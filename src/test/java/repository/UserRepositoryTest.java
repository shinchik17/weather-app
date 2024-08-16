package repository;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.util.HibernateTestUtil;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// todo: cover other repository tests
public class UserRepositoryTest {
    SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
    UserRepository userRepo = new UserRepository(sessionFactory);
    User transientUser;

    long PERSIST_USER_ID = 1L;
    long NONEXISTENT_USER_ID = 100L;


    @BeforeEach
    void setup() {
        transientUser = User.builder()
                .login("test_login1")
                .password("test_pass1")
                .build();
    }

    @Test()
    void save() {
        userRepo.save(transientUser);
        Assertions.assertTrue(userRepo.findById(transientUser.getId()).isPresent());
    }

    @Test
    void find_ifExists() {
        Assertions.assertTrue(userRepo.findById(1L).isPresent());
    }

    @Test
    void find_ifNotExists() {
        Assertions.assertFalse(userRepo.findById(NONEXISTENT_USER_ID).isPresent());
    }

    @Test
    void update_ifExists() {
        User user = userRepo.findById(PERSIST_USER_ID).orElseThrow();
        user.setLogin("MERGED_LOGIN");
        userRepo.update(user);
        Assertions.assertEquals(user.getLogin(), "MERGED_LOGIN");

    }

    @Test
    void update_ifNotExists() {
        transientUser.setLogin("MERGED_LOGIN");
        Assertions.assertThrows(BaseRepositoryException.class, () -> userRepo.update(transientUser));
    }



}
