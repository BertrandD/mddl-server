package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.inventory.ItemContainer;

/**
 * @author Leboc Philippe.
 */
public interface ItemContainerService extends DefaultService<ItemContainer> {
    ItemContainer create(Base base, String itemId);
}
