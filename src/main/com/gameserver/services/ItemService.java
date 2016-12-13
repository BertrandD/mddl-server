package com.gameserver.services;

import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
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

        final IInventory inventory = (IInventory) params[0];
        final String itemId = (String) params[1];
        final long count = (long) params[2];
        final ItemInstance item = new ItemInstance(inventory, itemId, count);
        mongoOperations.insert(item);
        return item;
    }

    public ItemInstance findOneBy(BaseInventory inventory, String templateId) {
        return findOneBy(Criteria.where("inventory.$id").is(new ObjectId(inventory.getId())).andOperator(Criteria.where("templateId").is(templateId)));
    }
}
