package com.gameserver.services;

import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.ItemInstance;

/**
 * @author Leboc Philippe.
 */
public interface ItemService extends DefaultService<ItemInstance> {
    ItemInstance create(IInventory inventory, String itemId, long count);
}
