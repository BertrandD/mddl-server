package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.PlayerDao;
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
public class BaseServiceImpl extends DefaultServiceImpl<Base, BaseDao> implements BaseService {

    @Autowired
    private BaseDao repository;

    @Autowired
    private BaseInventoryService baseInventoryService;

    @Autowired
    private PlayerDao playerDao;

    public Base create(String name, Player player, Planet planet) {

        Base base = new Base(name, player, planet);

        player.addBase(base);
        player.setCurrentBase(base);

        // Create new objects
        base = repository.save(base);
        final BaseInventory inventory = baseInventoryService.create(base);

        base.setBaseInventory(inventory);
        repository.save(base);

        planet.addBase(base);

        // astralObjectService.update(planet);

        playerDao.save(player);

        return base;
    }

    @Override
    public List<Base> findAll() {
        final List<Base> bases = repository.findAll();
        for (Base base : bases) {
            base.initializeStats();
            //inventoryService.refreshResources(base.getResources()); TODO: HANDLE ME !
        }
        return bases;
    }
}
