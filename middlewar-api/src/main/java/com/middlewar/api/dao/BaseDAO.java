package com.middlewar.api.dao;

import com.middlewar.core.model.Base;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service
public class BaseDAO implements DAO<Base> {
    private Vector<Base> bases = new Vector<>();

    @Override
    public void add(Base o) {
        bases.add(o);
    }

    @Override
    public void remove(Base o) {
        bases.remove(o);
    }

    @Override
    public Vector<Base> getAll() {
        return bases;
    }

    @Override
    public int count() {
        return bases.size();
    }

    @Override
    public Base getById(int i) {
        try {
            return bases.get(i - 1);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteAll() {
        bases.clear();
    }
}
