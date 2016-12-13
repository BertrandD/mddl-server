package com.gameserver.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.ItemContainer;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemContainerService extends DatabaseService<ItemContainer> {

    protected ItemContainerService() {
        super(ItemContainer.class);
    }

    @Override
    public ItemContainer create(Object... params) {
        if(params.length != 2) return null;
        final Base base = (Base) params[0];
        final String itemId = (String) params[1];

        final ItemInstance item = new ItemInstance(itemId, 0);
        final ItemContainer inventory = new ItemContainer(base, item);
        item.setInventory(inventory);

        mongoOperations.insert(item);
        mongoOperations.insert(inventory);

        base.addResourceInventory(inventory);
        mongoOperations.save(base);
        return inventory;
    }
}
