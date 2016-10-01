package com.gameserver.interfaces;

import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;

/**
 * @author LEBOC Philippe
 */
public interface IInventoryService {

    //ItemInstance addItem(ItemInstance item, final long amount);
    ItemInstance addItem(Inventory inventory, String templateId, final long amount);

    boolean consumeItem(ItemInstance item, final long amount);
    //boolean consumeItem(IInventory inventory, String id, final long amount);
}
