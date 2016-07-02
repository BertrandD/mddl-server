package com.gameserver.services;

import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.Inventory;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemService extends DatabaseService<ItemInstance> {

    protected ItemService() {
        super(ItemInstance.class);
    }

    @Override
    public ItemInstance create(Object... params) {
        if(params.length != 3) return null;

        final Inventory inventory = (Inventory) params[0];
        final String itemId = (String) params[1];
        final long count = (long) params[2];
        final ItemInstance item = new ItemInstance(inventory, itemId, count);
        mongoOperations.insert(item);
        return item;
    }

    public ItemInstance findOneBy(Inventory inventory, String templateId) {
        return findOneBy(Criteria.where("inventory").is(new ObjectId(inventory.getId())), Criteria.where("templateId").is(templateId));
    }
}
