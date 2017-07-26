package com.middlewar.core.interfaces;

import com.middlewar.core.model.instances.ItemInstance;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public interface IInventory {

    /**
     * @return
     */
    long getId();


    /**
     * @return
     */
    long getAvailableCapacity();


    /**
     * @param templateId
     * @return
     */
    ItemInstance getItem(String templateId);


    /**
     * @return
     */
    List<ItemInstance> getItems();
}
