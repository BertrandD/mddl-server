package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.inventory.BaseInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseService extends DatabaseService<Base> {

    @Autowired
    private InventoryService inventoryService;

    protected BaseService() {
        super(Base.class);
    }

    @Override
    public Base create(Object... params) {
        if(params.length != 2) return null;

        final String name = (String) params[0];
        final Player player = (Player) params[1];
        final Base base = new Base(name, player);
        final BaseInventory inventory = new BaseInventory(base);

        base.setBaseInventory(inventory);

        player.addBase(base);
        player.setCurrentBase(base);

        mongoOperations.insert(base);
        mongoOperations.insert(inventory);
        mongoOperations.save(player);
        return base;
    }

    @Override
    public Base findOne(String id) {
        final Base base = super.findOne(id);
        if(base != null) {
            base.initializeStats();
            inventoryService.refresh(base);
        }
        return base;
    }

    @Override
    public List<Base> findAll() {
        final List<Base> bases = super.findAll();
        for (Base base : bases) {
            base.initializeStats();
            inventoryService.refresh(base);
        }
        return bases;
    }
}
