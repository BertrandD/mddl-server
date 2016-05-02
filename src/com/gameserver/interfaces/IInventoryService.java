package com.gameserver.interfaces;

import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;

/**
 * @author LEBOC Philippe
 */
public interface IInventoryService {

    ItemInstance addItem(Inventory inventory, String itemId, long count);
    ItemInstance addNewItem(Inventory inventory, String itemId, long count);

    boolean consumeItem(ItemInstance item, long count);
    boolean consumeItem(String id, long count);
}
