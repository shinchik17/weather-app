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
    long DELETE_USER_ID = 4L;
    long NONEXISTENT_USER_ID = 100L;
    String UPDATED_LOGIN = "MERGED_LOGIN";


    @BeforeEach
    void setup() {
        transientUser = User.builder()
                .login("test_transient")
                .password("transient_pass")
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
        user.setLogin(UPDATED_LOGIN);
        userRepo.update(user);
        Assertions.assertEquals(userRepo.findById(PERSIST_USER_ID).orElseThrow().getLogin(), UPDATED_LOGIN);

    }

    @Test
    void update_ifNotExists() {
        transientUser.setLogin(UPDATED_LOGIN);
        Assertions.assertThrows(BaseRepositoryException.class, () -> userRepo.update(transientUser));
    }

    @Test
    void delete_ifExists() {
        userRepo.delete(DELETE_USER_ID);
        Assertions.assertTrue(userRepo.findById(DELETE_USER_ID).isEmpty(), UPDATED_LOGIN);
    }

    @Test
    void delete_ifNotExists() {
        Assertions.assertThrows(BaseRepositoryException.class, () -> userRepo.delete(NONEXISTENT_USER_ID));
    }



}
