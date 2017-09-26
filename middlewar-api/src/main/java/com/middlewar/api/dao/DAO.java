package com.middlewar.api.dao;

import java.util.List;

public interface DAO<T> {
    void add(T o);
    void remove(T o);
    List<T> getAll();
    int count();
    T getById(int i);
    void deleteAll();
}
