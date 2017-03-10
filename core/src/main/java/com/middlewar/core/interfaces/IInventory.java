package com.middlewar.core.interfaces;

import com.middlewar.core.model.instances.ItemInstance;

/**
 * @author LEBOC Philippe
 */
public interface IInventory {
    String getId();
    long getAvailableCapacity();
    ItemInstance getItem(String id);
}
