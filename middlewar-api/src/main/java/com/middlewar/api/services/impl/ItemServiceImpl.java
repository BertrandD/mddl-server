package com.middlewar.api.services.impl;

import com.middlewar.api.dao.ItemDao;
import com.middlewar.api.services.ItemService;
import com.middlewar.core.interfaces.IInventoryService;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private IInventoryService inventoryService;

    @Override
    public ItemInstance create(Inventory inventory, String itemId, long count) {
        final ItemInstance inst =  itemDao.save(new ItemInstance(inventory, itemId, count));
        inventory.getItems().add(inst);
        inventoryService.update(inventory);
        return inst;
    }

    @Override
    public ItemInstance findOne(long id) {
        return itemDao.findOne(id);
    }

    @Override
    public List<ItemInstance> findAll() {
        return itemDao.findAll();
    }

    @Override
    public void update(ItemInstance object) {
        itemDao.save(object);
    }

    @Override
    public void remove(ItemInstance object) {
        itemDao.delete(object);
    }

    @Override
    public void deleteAll() {
        itemDao.deleteAll();
    }
}
