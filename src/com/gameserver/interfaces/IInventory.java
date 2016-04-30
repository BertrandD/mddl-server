package com.gameserver.interfaces;

import com.gameserver.model.instances.ItemInstance;

/**
 * @author LEBOC Philippe
 */
public interface IInventory {

    long getMaxWeight();
    long getWeight();
    long getFreeWeight();

    long getMaxVolume();
    long getVolume();
    long getFreeVolume();

    boolean addItem(String id, long count);
    boolean addItem(ItemInstance item);

    ItemInstance consumeItem(ItemInstance item);
    ItemInstance consumeItem(String id, long count);
}
