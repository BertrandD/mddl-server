package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseInventoryDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.api.services.BaseInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service not used but we will keep it. If in few months we dont use, we will drop it
 * @author LEBOC Philippe
 */
@Service
public class BaseInventoryServiceImpl implements BaseInventoryService{

    @Autowired
    private BaseInventoryDao baseInventoryDao;

    @Override
    public BaseInventory create(Base base) {
        return baseInventoryDao.save(new BaseInventory(base));
    }

    @Override
    public BaseInventory findOne(String id) {
        return baseInventoryDao.findOne(id);
    }

    @Override
    public List<BaseInventory> findAll() {
        return baseInventoryDao.findAll();
    }

    @Override
    public void update(BaseInventory object) {
        baseInventoryDao.save(object);
    }

    @Override
    public void remove(BaseInventory object) {
        baseInventoryDao.delete(object);
    }

    @Override
    public void deleteAll() {
        baseInventoryDao.deleteAll();
    }
}
