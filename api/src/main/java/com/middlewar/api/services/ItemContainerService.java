package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.ResourceInventory;

/**
 * @author Leboc Philippe.
 */
public interface ItemContainerService extends DefaultService<ResourceInventory> {
    ResourceInventory create(Base base, String itemId);
}
