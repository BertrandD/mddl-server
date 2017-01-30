package com.middlewar.api.gameserver.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.inventory.BaseInventory;
import org.springframework.stereotype.Service;

/**
 * Service not used but we will keep it. If in few months we dont use, we will drop it
 * @author LEBOC Philippe
 */
@Service
public class BaseInventoryService extends DatabaseService<BaseInventory> {

    protected BaseInventoryService() {
        super(BaseInventory.class);
    }

    @Override
    public BaseInventory create(Object... params) {
        if(params.length != 1)
            return null;
        final BaseInventory inventory = new BaseInventory((Base)params[0]);
        mongoOperations.insert(inventory);
        return inventory;
    }
}
