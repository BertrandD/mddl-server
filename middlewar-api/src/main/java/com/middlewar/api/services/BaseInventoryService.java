package com.middlewar.api.services;

import com.middlewar.api.dao.BaseInventoryDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.BaseInventory;

/**
 * @author Leboc Philippe.
 */
public interface BaseInventoryService extends DefaultService<BaseInventory, BaseInventoryDao> {
    BaseInventory create(Base base);
}
