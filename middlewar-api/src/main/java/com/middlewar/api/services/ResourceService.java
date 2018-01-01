package com.middlewar.api.services;

import com.middlewar.core.config.Config;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

    @Autowired
    private ItemService itemService;

    public Resource create(Base base, String itemId) {
        final ItemInstance item = itemService.create(base.getBaseInventory(), itemId, 0);
        final Resource resource = new Resource(base, item);
        base.addResource(resource);
        // original production = 0
        base.getBaseStat().unlock(resource.getStat());
        // original max stored = BASE_INITIAL_MAX_RESOURCE_STORAGE
        base.getBaseStat().unlock(resource.getStatMax(), Config.BASE_INITIAL_MAX_RESOURCE_STORAGE, StatOp.DIFF);

        return resource;
    }
}
