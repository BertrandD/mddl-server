package com.gameserver.interfaces;

import com.gameserver.model.instances.ItemInstance;

/**
 * @author LEBOC Philippe
 */
public interface IInventory {

    boolean isAllowedToStore(ItemInstance item);

    long getMaxCapacity();

    long getFreeCapacity();

    long getCurrentCapacityCharge();

    boolean addItem(ItemInstance item);

    ItemInstance consumeItem(ItemInstance item);

    ItemInstance consumeItem(String id, long count);
}
