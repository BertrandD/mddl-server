package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.ItemDao;
import com.middlewar.api.dao.ResourceDao;
import com.middlewar.api.services.ResourceService;
import com.middlewar.core.enums.StatOp;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.stats.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceDao resourceDao;

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private ItemDao itemDao;

    @Override
    public Resource create(Base base, String itemId) {
        final ItemInstance item = new ItemInstance(itemId, 0);
        final Resource resource = new Resource(base, item);
        resource.setStat(Stats.MAX_RESOURCE_1); // TODO: replace with: resource.setStat(Stats.valueOf("MAX_"+itemId.toUpperCase()));
        base.addResource(resource);
        base.getBaseStat().add(resource.getStat(), Stats.MAX_RESOURCE_1.getValue(), StatOp.UNLOCK);
        baseDao.save(base);

        return resourceDao.save(resource);
    }

    @Override
    public Resource findOne(String id) {
        return resourceDao.findOne(id);
    }

    @Override
    public List<Resource> findAll() {
        return resourceDao.findAll();
    }

    @Override
    public void update(Resource object) {
        itemDao.save(object.getItem());
        resourceDao.save(object);
    }

    @Override
    public void remove(Resource object) {
        resourceDao.delete(object);
    }

    @Override
    public void deleteAll() {
        resourceDao.deleteAll();
    }
}
