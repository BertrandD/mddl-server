package com.middlewar.api.dao;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Bertrand
 * Copy of org.springframework.data.mongodb.repository.MongoRepository
 */
@NoRepositoryBean
public interface DefaultRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    <S extends T> List<S> save(Iterable<S> var1);

    List<T> findAll();
}
