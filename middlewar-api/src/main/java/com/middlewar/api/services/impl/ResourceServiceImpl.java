package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.ResourceDao;
import com.middlewar.api.services.ItemService;
import com.middlewar.api.services.ResourceService;
import com.middlewar.core.config.Config;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ResourceServiceImpl extends DefaultServiceImpl<Resource, ResourceDao> implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private ItemService itemService;

    @Override
    public Resource create(Base base, String itemId) {
        final ItemInstance item = itemService.create(base.getBaseInventory(), itemId, 0);
        final Resource resource = resourceDao.save(new Resource(base, item));
        base.addResource(resource);
        // original production = 0
        base.getBaseStat().unlock(resource.getStat());
        // original max stored = BASE_INITIAL_MAX_RESOURCE_STORAGE
        base.getBaseStat().unlock(resource.getStatMax(), Config.BASE_INITIAL_MAX_RESOURCE_STORAGE, StatOp.DIFF);
        baseDao.save(base);

        return resource;
    }
}