package com.middlewar.api.services.impl;

import com.middlewar.api.dao.ItemDao;
import com.middlewar.api.services.ItemService;
import com.middlewar.core.interfaces.IInventoryService;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemServiceImpl extends DefaultServiceImpl<ItemInstance, ItemDao> implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private IInventoryService inventoryService;

    @Override
    public ItemInstance create(Inventory inventory, String itemId, long count) {
        final ItemInstance inst = itemDao.save(new ItemInstance(inventory, itemId, count));
        inventory.getItems().add(inst);
        inventoryService.update(inventory);
        return inst;
    }
}
