package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.inventory.Resource;

/**
 * @author Leboc Philippe.
 */
public interface InventoryService extends CrudService<Inventory, Integer> {

    Resource createNewResource(final Base base, final String templateId);
    ItemInstance addItem(final Inventory inventory, final String templateId, final long amount);
    boolean addResource(Resource resource, double amount);
    boolean consumeItem(ItemInstance item, final long amount);
}
