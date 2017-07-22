package com.middlewar.api.dao;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Bertrand
 * Copy of org.springframework.data.mongodb.repository.MongoRepository
 */
@NoRepositoryBean
public interface DefaultRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    <S extends T> List<S> save(Iterable<S> var1);

    List<T> findAll();

    List<T> findAll(Sort var1);

    <S extends T> S insert(S var1);

    <S extends T> List<S> insert(Iterable<S> var1);

    <S extends T> List<S> findAll(Example<S> var1);

    <S extends T> List<S> findAll(Example<S> var1, Sort var2);
}
