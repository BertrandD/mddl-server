package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.ResourceInventory;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ResourceInventoryService extends DatabaseService<ResourceInventory> {

    protected ResourceInventoryService() {
        super(ResourceInventory.class);
    }

    @Override
    public ResourceInventory create(Object... params) {
        if(params.length != 2) return null;
        final Base base = (Base) params[0];
        final String itemId = (String) params[1];

        final ItemInstance item = new ItemInstance(itemId, 0);
        final ResourceInventory inventory = new ResourceInventory(base, item);
        item.setInventory(inventory);

        mongoOperations.insert(item);
        mongoOperations.insert(inventory);

        base.addResourceInventory(inventory);
        mongoOperations.save(base);
        return inventory;
    }
}
