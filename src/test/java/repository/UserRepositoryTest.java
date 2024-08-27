package repository;


import com.alexshin.weatherapp.entity.User;
import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.repository.UserRepository;
import com.alexshin.weatherapp.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class UserRepositoryTest {
    SessionFactory sessionFactory;
    UserRepository userRepo;
    User transientUser;

    long PERSIST_USER_ID = 1L;
    long DELETE_USER_ID = 4L;
    long NONEXISTENT_USER_ID = 100L;
    String PERSIST_USER_LOGIN = "test_username1";
    String UPDATED_LOGIN = "MERGED_LOGIN";


    @BeforeEach
    void setup() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        userRepo = new UserRepository(sessionFactory);

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

    @Test()
    void save_ifLoginExists_thenThrow() {
        User transientUser2 = User.builder()
                .login(PERSIST_USER_LOGIN)
                .password("transient_pass")
                .build();
        Assertions.assertThrows(BaseRepositoryException.class, () -> userRepo.save(transientUser2));
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
    void update_ifNotExists_thenThrow() {
        transientUser.setLogin(UPDATED_LOGIN);
        Assertions.assertThrows(BaseRepositoryException.class, () -> userRepo.update(transientUser));
    }

    @Test
    void delete_ifExists() {
        userRepo.delete(DELETE_USER_ID);
        Assertions.assertTrue(userRepo.findById(DELETE_USER_ID).isEmpty(), UPDATED_LOGIN);
    }

    @Test
    void delete_ifNotExists_thenThrow() {
        Assertions.assertThrows(BaseRepositoryException.class, () -> userRepo.delete(NONEXISTENT_USER_ID));
    }

    @Test
    void findAll_ifExist() {
        Assertions.assertFalse(userRepo.findAll().isEmpty());
    }


}
