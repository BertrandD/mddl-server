package com.gameserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public abstract class DatabaseService<T> {

    @Autowired
    protected MongoOperations mongoOperations;

    private Class<T> clazz;

    protected DatabaseService(Class<T> clazz) {
        setClazz(clazz);
    }

    public abstract T create(Object... params);

    public T findOne(String id) {
        return mongoOperations.findById(id, getClazz());
    }

    public T findOne(T object) {
        return mongoOperations.findOne(new Query(Criteria.where("id").is(object)), getClazz());
    }

    public T findOneBy(Criteria... criterias) {
        return findOneBy(null, criterias);
    }

    public T findOneBy(Sort sort, Criteria... criterias) {
        final Query query = new Query();
        for (Criteria criteria : criterias) {
            query.addCriteria(criteria);
        }

        if(sort != null) {
            query.with(sort);
        }

        return mongoOperations.findOne(query, getClazz());
    }

    public List<T> findAll() {
        return mongoOperations.findAll(getClazz());
    }

    public List<T> findBy(Criteria... criterias) {
        return findBy(null, criterias);
    }

    public List<T> findBy(Sort sort, Criteria... criterias) {
        final Query query = new Query();
        for (Criteria criteria : criterias) {
            query.addCriteria(criteria);
        }

        if(sort != null) {
            query.with(sort);
        }

        return mongoOperations.find(query, getClazz());
    }

    public List<T> findBy(int limit, Criteria... criterias) {
        return findBy(null, limit, criterias);
    }

    public List<T> findBy(Sort sort, int limit, Criteria... criterias) {
        final Query query = new Query();
        for (Criteria criteria : criterias) {
            query.addCriteria(criteria);
        }

        if(sort != null) {
            query.with(sort);
        }

        query.limit(limit);

        return mongoOperations.find(query, getClazz());
    }

    public void update(T object) {
        mongoOperations.save(object);
    }

    @Async
    public void updateAsync(T object) {
        mongoOperations.save(object);
    }

    @Async
    public void deleteAsync(T object) {
        mongoOperations.remove(object);
    }

    public void delete(T object) {
        mongoOperations.remove(object);
    }

    @Async
    public void deleteAsync(String id) {
        mongoOperations.remove(new Query(Criteria.where("id").is(id)), getClazz());
    }

    @Async
    public void deleteAll() {
        mongoOperations.remove(new Query(), getClazz());
    }

    protected Class<T> getClazz() {
        return clazz;
    }

    private void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
}
