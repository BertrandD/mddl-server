package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.PlayerDao;
import com.middlewar.api.services.AstralObjectService;
import com.middlewar.api.services.BaseInventoryService;
import com.middlewar.api.services.BaseService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.space.Planet;
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
    private BaseInventoryService baseInventoryService;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private AstralObjectService astralObjectService;

    public Base create(String name, Player player, Planet planet) {

        Base base = new Base(name, player, planet);

        player.addBase(base);
        player.setCurrentBase(base);

        // Create new objects
        base = baseDao.save(base);
        final BaseInventory inventory = baseInventoryService.create(base);

        base.setBaseInventory(inventory);
        baseDao.save(base);

        planet.addBase(base);
//        astralObjectService.update(planet);

        playerDao.save(player);

        return base;
    }

    @Override
    public Base findOne(long id) {
        return baseDao.findOne(id);
    }

    @Override
    public List<Base> findAll() {
        final List<Base> bases = baseDao.findAll();
        for (Base base : bases) {
            base.initializeStats();
            //inventoryService.refresh(base.getResources()); TODO: HANDLE ME !
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
    public void deleteAll() {
        baseDao.deleteAll();
    }
}
