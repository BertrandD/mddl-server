package com.gameserver.interfaces;

import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;

/**
 * @author LEBOC Philippe
 */
public interface IInventoryService {

    ItemInstance addItem(Inventory inventory, String templateId, long count);

    boolean consumeItem(ItemInstance item, long count);
    boolean consumeItem(Inventory inventory, String id, long count);
}
