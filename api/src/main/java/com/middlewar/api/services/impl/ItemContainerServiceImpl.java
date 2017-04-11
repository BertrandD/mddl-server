package com.gameserver.services.impl;

import com.gameserver.dao.BaseDao;
import com.gameserver.dao.ItemContainerDao;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.ItemContainer;
import com.gameserver.services.ItemContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemContainerServiceImpl implements ItemContainerService {

    @Autowired
    private ItemContainerDao itemContainerDao;

    @Autowired
    private BaseDao baseDao;

    @Override
    public ItemContainer create(Base base, String itemId) {
        final ItemInstance item = new ItemInstance(itemId, 0);
        final ItemContainer inventory = new ItemContainer(base, item);
        item.setInventory(inventory);

        // TODO: move this logic
        itemContainerDao.insert(inventory);

        base.addResourceInventory(inventory);
        baseDao.save(base);

        return inventory;
    }

    @Override
    public ItemContainer findOne(String id) {
        return itemContainerDao.findOne(id);
    }

    @Override
    public List<ItemContainer> findAll() {
        return itemContainerDao.findAll();
    }

    @Override
    public void update(ItemContainer object) {
        itemContainerDao.save(object);
    }

    @Override
    public void remove(ItemContainer object) {
        itemContainerDao.delete(object);
    }

    @Override
    public void clearAll() {
        itemContainerDao.deleteAll();
    }
}
