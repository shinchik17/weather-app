package com.alexshin.weatherapp.repository;

import com.alexshin.weatherapp.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public interface Repository<K extends Serializable, E extends BaseEntity<K>>{


    E save(E entity);

    void update(E entity);

    void delete(K id);

    default Optional<E> findById(K id) {
        return findById(id, emptyMap());
    }

    Optional<E> findById(K id, Map<String, Object> properties);

    List<E> findAll();

}
