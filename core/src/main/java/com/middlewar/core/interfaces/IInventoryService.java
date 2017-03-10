package com.middlewar.core.interfaces;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;

/**
 * @author LEBOC Philippe
 */
public interface IInventoryService {
    Resource createNewResource(final Base base, final String templateId);
    ItemInstance addItem(final IInventory inventory, final String templateId, final long amount);
    boolean addResource(Resource resource, long amount);
    boolean consumeItem(ItemInstance item, final long amount);
    boolean consumeResource(Resource resource, final long amount);
}
