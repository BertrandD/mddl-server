package com.middlewar.api.dao;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Bertrand
 */
@NoRepositoryBean
public interface DefaultRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
    <S extends T> List<S> save(Iterable<S> var1);

    List<T> findAll();
}
