package com.alexshin.weatherapp.repository;


import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.model.entity.UserSession;
import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


public class UserSessionRepositoryTest {
    SessionFactory sessionFactory;
    UserSessionRepository userSessionRepo;
    UserSession transientUserSession;
    UserSession transientUserSession2;

    User PERSISTENT_USER = User.builder()
            .id(1L)
            .login("test_username1")
            .password("test_pass1")
            .build();

    String NONEXISTENT_USER_LOGIN = "";
    String NONEXISTENT_SESSION_ID = "test_session_id100";
    String PERSISTENT_SESSION_ID = "test_session_id1";
    String TRANSIENT_SESSION_ID = "transient_session_id";
    LocalDateTime PERSISTENT_EXPIRES_AT = LocalDateTime.parse("2001-09-11T00:00");
    LocalDateTime UPDATED_EXPIRES_AT = LocalDateTime.parse("2001-09-11T01:00");

    @BeforeEach
    void setup() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        userSessionRepo = new UserSessionRepository(sessionFactory);


        transientUserSession = UserSession.builder()
                .id(TRANSIENT_SESSION_ID)
                .user(PERSISTENT_USER)
                .expiresAt(PERSISTENT_EXPIRES_AT)
                .build();

    }

    @Test()
    void save() {
        userSessionRepo.save(transientUserSession);
        Assertions.assertTrue(userSessionRepo.findById(transientUserSession.getId()).isPresent());
    }

    @Test()
    void save_ifIdExists_thenThrow() {
        transientUserSession2 = UserSession.builder()
                .id(PERSISTENT_SESSION_ID)
                .user(PERSISTENT_USER)
                .expiresAt(PERSISTENT_EXPIRES_AT)
                .build();
        Assertions.assertThrows(BaseRepositoryException.class, () -> userSessionRepo.save(transientUserSession2));
    }

    @Test
    void find_ifExists() {
        Assertions.assertTrue(userSessionRepo.findById(PERSISTENT_SESSION_ID).isPresent());
    }

    @Test
    void find_ifNotExists() {
        Assertions.assertFalse(userSessionRepo.findById(NONEXISTENT_SESSION_ID).isPresent());
    }

    @Test
    void update_ifExists() {
        UserSession userSession = userSessionRepo.findById(PERSISTENT_SESSION_ID).orElseThrow();
        userSession.setExpiresAt(UPDATED_EXPIRES_AT);

        userSessionRepo.update(userSession);
        LocalDateTime updatedExpiresAt = userSessionRepo.findById(PERSISTENT_SESSION_ID).orElseThrow().getExpiresAt();
        Assertions.assertEquals(UPDATED_EXPIRES_AT, updatedExpiresAt);

    }

    @Test
    void update_ifNotExists_thenThrow() {
        transientUserSession.setExpiresAt(UPDATED_EXPIRES_AT);
        Assertions.assertTrue(userSessionRepo.findById(transientUserSession.getId()).isEmpty());
    }

    @Test
    void delete_ifExists() {
        userSessionRepo.delete(PERSISTENT_SESSION_ID);
        Assertions.assertTrue(userSessionRepo.findById(PERSISTENT_SESSION_ID).isEmpty());
    }

    @Test
    void delete_ifNotExists_thenThrow() {
        Assertions.assertThrows(BaseRepositoryException.class, () -> userSessionRepo.delete(NONEXISTENT_SESSION_ID));
    }

    @Test
    void deleteByUserLogin_ifExists() {
        userSessionRepo.deleteByUserLogin(PERSISTENT_USER.getLogin());
        Assertions.assertTrue(userSessionRepo.findById(PERSISTENT_SESSION_ID).isEmpty());
    }

    @Test
    void deleteByUserLogin_ifNotExists() {
        Assertions.assertDoesNotThrow(() -> userSessionRepo.deleteByUserLogin(NONEXISTENT_USER_LOGIN));
    }

    @Test
    void findAll_ifAnyExist() {
        Assertions.assertFalse(userSessionRepo.findAll().isEmpty());
    }

    @Test
    void findAll_ifNoAnyExist() {
        userSessionRepo.delete("test_session_id1");
        userSessionRepo.delete("test_session_id2");
        Assertions.assertTrue(userSessionRepo.findAll().isEmpty());
    }


}
