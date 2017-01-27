package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.ItemContainer;

/**
 * @author Leboc Philippe.
 */
public interface ItemContainerService extends DefaultService<ItemContainer> {
    ItemContainer create(Base base, String itemId);
}
