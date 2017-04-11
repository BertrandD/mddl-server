package com.gameserver.services.impl;

import com.gameserver.dao.ItemDao;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDao itemDao;

    @Override
    public ItemInstance create(IInventory inventory, String itemId, long count) {
        return itemDao.insert(new ItemInstance(inventory, itemId, count));
    }

    public ItemInstance findOneBy(BaseInventory inventory, String templateId) {
        return null;
        // TODO: work on me !
        // findOneBy(Criteria.where("inventory.$id").is(new ObjectId(inventory.getId())).andOperator(Criteria.where("templateId").is(templateId)));
    }

    @Override
    public ItemInstance findOne(String id) {
        return itemDao.findOne(id);
    }

    @Override
    public List<ItemInstance> findAll() {
        return itemDao.findAll();
    }

    @Override
    public void update(ItemInstance object) {
        itemDao.save(object);
    }

    @Override
    public void remove(ItemInstance object) {
        itemDao.delete(object);
    }

    @Override
    public void clearAll() {
        itemDao.deleteAll();
    }
}
