package com.alexshin.weatherapp.repository;


import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.model.entity.User;
import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class LocationRepositoryTest {
    SessionFactory sessionFactory;
    LocationRepository locationRepository;
    Location transientLocation;
    Location transientLocation2;

    User PERSISTENT_USER = User.builder()
            .id(1L)
            .login("test_username1")
            .password("test_pass1")
            .build();

    long NONEXISTENT_USER_ID = 100L;
    long NONEXISTENT_LOCATION_ID = 100L;
    long PERSISTENT_LOCATION_ID = 1L;
    String TRANSIENT_LOCATION_NAME = "transient_loc_name1";
    BigDecimal PERSISTENT_LONGITUDE = BigDecimal.valueOf(1.0d);
    BigDecimal UPDATED_LONGITUDE = BigDecimal.valueOf(2.0d);

    @BeforeEach
    void setup() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        locationRepository = new LocationRepository(sessionFactory);


        transientLocation = Location.builder()
                .name(TRANSIENT_LOCATION_NAME)
                .user(PERSISTENT_USER)
                .longitude(PERSISTENT_LONGITUDE)
                .latitude(PERSISTENT_LONGITUDE)
                .build();

    }

    @Test()
    void save() {
        locationRepository.save(transientLocation);
        Assertions.assertTrue(locationRepository.findById(transientLocation.getId()).isPresent());
    }


    @Test
    void find_ifExists() {
        Assertions.assertTrue(locationRepository.findById(PERSISTENT_LOCATION_ID).isPresent());
    }

    @Test
    void find_ifNotExists() {
        Assertions.assertFalse(locationRepository.findById(NONEXISTENT_LOCATION_ID).isPresent());
    }

    @Test
    void update_ifExists() {
        Location location = locationRepository.findById(PERSISTENT_LOCATION_ID).orElseThrow();
        location.setLongitude(UPDATED_LONGITUDE);

        locationRepository.update(location);
        BigDecimal updatedLongitude = locationRepository.findById(PERSISTENT_LOCATION_ID).orElseThrow().getLongitude();
        Assertions.assertEquals(UPDATED_LONGITUDE, updatedLongitude);
    }

    @Test
    void update_ifNotExists_thenThrow() {
        transientLocation.setLongitude(UPDATED_LONGITUDE);
        Assertions.assertThrows(BaseRepositoryException.class, () -> locationRepository.update(transientLocation));
    }

    @Test
    void delete_ifExists() {
        locationRepository.delete(PERSISTENT_LOCATION_ID);
        Assertions.assertTrue(locationRepository.findById(PERSISTENT_LOCATION_ID).isEmpty());
    }

    @Test
    void delete_ifNotExists_thenThrow() {
        Assertions.assertThrows(BaseRepositoryException.class, () -> locationRepository.delete(NONEXISTENT_LOCATION_ID));
    }

    @Test
    void findAll_ifAnyExist() {
        Assertions.assertFalse(locationRepository.findAll().isEmpty());
    }

    @Test
    void findAll_ifNoAnyExist() {
        locationRepository.delete(1L);
        locationRepository.delete(2L);
        Assertions.assertTrue(locationRepository.findAll().isEmpty());
    }

    @Test
    void findByUserId_ifExists(){
        Assertions.assertFalse(locationRepository.findByUserId(PERSISTENT_USER.getId()).isEmpty());
    }

    @Test
    void findByUserId_ifNotExist(){
        Assertions.assertTrue(locationRepository.findByUserId(NONEXISTENT_USER_ID).isEmpty());
    }




}
