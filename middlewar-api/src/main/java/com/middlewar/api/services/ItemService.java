package com.middlewar.api.services;

import com.middlewar.api.dao.ItemDao;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;

/**
 * @author Leboc Philippe.
 */
public interface ItemService extends DefaultService<ItemInstance, ItemDao> {
    ItemInstance create(Inventory inventory, String itemId, long count);
}
