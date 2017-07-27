package com.middlewar.api.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface DefaultService<T, S extends JpaRepository<T, Long>> {

    T findOne(long id);

    List<T> findAll();

    void update(T object);

    void updateAsync(T object);

    void delete(T object);

    void deleteAsync(T object);

    void deleteAsync(long id);

    void deleteAll();

    void deleteAllAsync();
}
