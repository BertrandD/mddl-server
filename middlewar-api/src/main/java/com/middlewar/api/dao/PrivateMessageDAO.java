package com.middlewar.api.dao;

import com.middlewar.core.model.social.PrivateMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class PrivateMessageDAO implements DAO<PrivateMessage> {
    private Vector<PrivateMessage> pms = new Vector<>();

    @Override
    public void add(PrivateMessage o) {
        pms.add(o);
    }

    @Override
    public void remove(PrivateMessage o) {
        pms.remove(o);
    }

    @Override
    public List<PrivateMessage> getAll() {
        return pms;
    }

    @Override
    public int count() {
        return pms.size();
    }

    @Override
    public PrivateMessage getById(int i) {
        try {
            return pms.get(i - 1);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteAll() {
        pms.clear();
    }
}
