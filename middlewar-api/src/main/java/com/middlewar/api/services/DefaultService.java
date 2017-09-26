package com.middlewar.api.services;

public interface DefaultService<T> {
    void delete(T o);

    T findOne(int id);

    int nextId();
}
