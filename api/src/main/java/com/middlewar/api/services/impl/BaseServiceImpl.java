package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.BaseInventoryDao;
import com.middlewar.api.dao.PlayerDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.api.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private BaseInventoryDao baseInventoryDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private InventoryService inventoryService;

    public Base create(String name, Player player) {

        final Base base = new Base(name, player);
        final BaseInventory inventory = new BaseInventory(base);

        base.setBaseInventory(inventory);
        player.addBase(base);
        player.setCurrentBase(base);

        // Create new objects
        baseDao.insert(base);
        baseInventoryDao.insert(inventory);

        // Update existing objects
        playerDao.save(player);
        return base;
    }

    @Override
    public Base findOne(String id) {
        final Base base = baseDao.findOne(id);
        if(base != null) {
            base.initializeStats();
            inventoryService.refresh(base);
        }
        return base;
    }

    @Override
    public List<Base> findAll() {
        final List<Base> bases = baseDao.findAll();
        for (Base base : bases) {
            base.initializeStats();
            inventoryService.refresh(base);
        }
        return bases;
    }

    @Override
    public void update(Base object) {
        baseDao.save(object);
    }

    @Override
    public void remove(Base object) {
        baseDao.delete(object);
    }

    @Override
    public void clearAll() {
        baseDao.deleteAll();
    }
}
