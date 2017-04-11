package com.middlewar.api.services;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface DefaultService<T> {

    T findOne(String id);

    List<T> findAll();

    void update(T object);

    void remove(T object);

    void clearAll();
}
