package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.ResourceDao;
import com.middlewar.api.services.ItemService;
import com.middlewar.api.services.ResourceService;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.stats.Stats;
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
//        resource.setStat(Stats.valueOf(itemId.toUpperCase()));
        base.addResource(resource);
        // original production
        base.getBaseStat().add(Stats.valueOf(itemId.toUpperCase()), Stats.valueOf(itemId.toUpperCase()).getValue(), StatOp.UNLOCK);
        // original max stored
        base.getBaseStat().add(Stats.valueOf("MAX_" + itemId.toUpperCase()), Stats.valueOf("MAX_" + itemId.toUpperCase()).getValue(), StatOp.UNLOCK);
        baseDao.save(base);

        return resource;
    }
}
