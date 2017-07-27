package com.middlewar.api.services;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface DefaultService<T> {

    T findOne(long id);

    List<T> findAll();

    void update(T object);

    void remove(T object);

    void deleteAll();
}
