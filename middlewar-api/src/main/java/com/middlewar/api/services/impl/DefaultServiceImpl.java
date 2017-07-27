package com.middlewar.api.services.impl;

import com.middlewar.api.services.DefaultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public abstract class DefaultServiceImpl<T, S extends JpaRepository<T, Long>> implements DefaultService<T, S> {

    @Autowired
    protected S repository;

    @Override
    public T findOne(long id) {
        return repository.findOne(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(T object) {
        repository.save(object);
    }

    @Async
    @Override
    public void updateAsync(T object) {
        repository.save(object);
    }

    @Async
    @Override
    public void deleteAsync(T object) {
        repository.delete(object);
    }

    @Override
    public void delete(T object) {
        repository.delete(object);
    }

    @Async
    @Override
    public void deleteAsync(long id) {
        repository.delete(id);
    }

    @Async
    @Override
    public void deleteAllAsync() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
