package com.gameserver.services;

import com.gameserver.interfaces.IInventoryService;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class InventoryService implements IInventoryService {

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BaseInventoryService baseInventoryService;

    public ItemInstance addItem(Inventory inventory, String templateId, long count){
        // TODO: check inventory capacity before

        Utils.println("inv id = "+inventory.getId());
        Utils.println("templateId = "+templateId);

        final ItemInstance item = itemService.findFirstByInventoryAndTemplateId(inventory, templateId);
        Utils.println("Item is already exist ? "+(item != null));

        if(item == null) {
            return addNewItem(inventory, templateId, count);
        }

        item.setCount(item.getCount()+count);
        itemService.update(item);
        return item;
    }

    @Override
    public ItemInstance addNewItem(Inventory inventory, String templateId, long count) {
        final ItemInstance item = itemService.create(inventory, templateId, count);
        if(item == null) return null;

        inventory.getItems().add(item);

        if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory)inventory);
        else if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
        else return null;

        return item;
    }

    @Override
    public boolean consumeItem(ItemInstance item, long count) {
        return false;
    }

    @Override
    public boolean consumeItem(String id, long count) {
        return false;
    }
}
