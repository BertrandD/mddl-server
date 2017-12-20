package com.middlewar.api.services;

import com.middlewar.core.interfaces.IInventoryService;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemService implements DefaultService<ItemInstance> {

    private int nextId = 0;

    @Autowired
    private IInventoryService inventoryService;

    public ItemInstance create(Inventory inventory, String itemId, long count) {
        final ItemInstance inst = new ItemInstance(inventory, itemId, count);
        inst.setId(nextId());
        inventory.getItems().add(inst);
        return inst;
    }

    @Override
    public void delete(ItemInstance o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemInstance findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextId() {
        return ++nextId;
    }
}
