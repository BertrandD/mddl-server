package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.ItemContainerDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.ResourceInventory;
import com.middlewar.api.services.ItemContainerService;
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
    public ResourceInventory create(Base base, String itemId) {
        final ItemInstance item = new ItemInstance(itemId, 0);
        final ResourceInventory inventory = new ResourceInventory(base, item);
        item.setInventory(inventory);

        // TODO: move this logic
        itemContainerDao.insert(inventory);

        base.addResourceInventory(inventory);
        baseDao.save(base);

        return inventory;
    }

    @Override
    public ResourceInventory findOne(String id) {
        return itemContainerDao.findOne(id);
    }

    @Override
    public List<ResourceInventory> findAll() {
        return itemContainerDao.findAll();
    }

    @Override
    public void update(ResourceInventory object) {
        itemContainerDao.save(object);
    }

    @Override
    public void remove(ResourceInventory object) {
        itemContainerDao.delete(object);
    }

    @Override
    public void deleteAll() {
        itemContainerDao.deleteAll();
    }
}
