package com.middlewar.core.interfaces;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.inventory.Resource;

/**
 * @author LEBOC Philippe
 */
public interface IInventoryService {

    /**
     * @param base
     * @param templateId
     * @return
     */
    Resource createNewResource(final Base base, final String templateId);

    /**
     * @param inventory
     * @param templateId
     * @param amount
     * @return
     */
    ItemInstance addItem(final Inventory inventory, final String templateId, final long amount);

    /**
     * @param resource
     * @param amount
     * @return
     */
    boolean addResource(Resource resource, long amount);

    /**
     * @param item
     * @param amount
     * @return
     */
    boolean consumeItem(ItemInstance item, final long amount);
    
    /**
     * @param base
     */
    void refreshResources(final Base base);

    /**
     * @param resource
     */
    void refreshResources(final Resource resource);
}
