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

    boolean consumeItem(ItemInstance item, long count);
    boolean consumeItem(String id, long count);
}
