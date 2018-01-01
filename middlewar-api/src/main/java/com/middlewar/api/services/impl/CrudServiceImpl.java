package com.middlewar.api.services.impl;

import com.middlewar.api.services.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Slf4j
@Service
@Validated
public abstract class CrudServiceImpl<T, ID extends Serializable, R extends JpaRepository<T, ID>> implements CrudService<T, ID> {

    @Autowired
    protected R repository;

    @Override
    public T save(@NotNull @Valid T object) {
        return repository.save(object);
    }

    @Override
    public T find(ID id) {
        return repository.findOne(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T update(@NotNull @Valid T object) {
        return repository.save(object);
    }

    @Async
    @Override
    public void updateAsync(@NotNull @Valid T object) {
        final T result = repository.save(object);
        if(null == result) {
            log.warn("UpdateAsync failed to save generic object !");
        }
    }

    @Async
    @Override
    public void deleteAsync(@NotNull @Valid T object) {
        repository.delete(object);
    }

    @Override
    public void delete(@NotNull @Valid T object) {
        repository.delete(object);
    }

    @Async
    @Override
    public void deleteAsyncById(ID id) {
        repository.delete(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
