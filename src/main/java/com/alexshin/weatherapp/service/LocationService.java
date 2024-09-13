package com.alexshin.weatherapp.service;

import com.alexshin.weatherapp.exception.BaseRepositoryException;
import com.alexshin.weatherapp.exception.service.SuchLocationExistsException;
import com.alexshin.weatherapp.model.dto.LocationDTO;
import com.alexshin.weatherapp.model.dto.UserDTO;
import com.alexshin.weatherapp.model.entity.Location;
import com.alexshin.weatherapp.repository.LocationRepository;
import com.alexshin.weatherapp.util.HibernateUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;

public class LocationService {
    private static final LocationService INSTANCE = new LocationService();
    private final LocationRepository locationRepository = new LocationRepository(HibernateUtil.getSessionFactory());
    private final ModelMapper mapper = new ModelMapper();


    private LocationService() {
    }

    public static LocationService getInstance() {
        return INSTANCE;
    }


    public void saveLocation(LocationDTO location) {
        try {
            locationRepository.save(mapper.map(location, Location.class));
        } catch (BaseRepositoryException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new SuchLocationExistsException("Location %s already exists".formatted(location.getName()));
            }
            throw e;
        }

    }


    public List<LocationDTO> findByUser(UserDTO user) {
        return mapper.map(locationRepository.findByUserId(user.getId()), new TypeToken<List<LocationDTO>>() {
        }.getType());
    }

    public void deleteById(long id){
        locationRepository.delete(id);
    }


}
