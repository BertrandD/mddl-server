package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.inventory.BaseInventory;

/**
 * @author Leboc Philippe.
 */
public interface BaseInventoryService extends DefaultService<BaseInventory> {
    BaseInventory create(Base base);
}
