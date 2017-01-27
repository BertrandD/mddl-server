package com.middlewar.api.services;

import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.instances.ItemInstance;

/**
 * @author Leboc Philippe.
 */
public interface ItemService extends DefaultService<ItemInstance> {
    ItemInstance create(IInventory inventory, String itemId, long count);
}
