package com.gameserver.services;

import com.gameserver.interfaces.IInventoryService;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.inventory.ResourceInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

    @Autowired
    private ResourceInventoryService resourceInventoryService;

    public void update(Inventory inventory){
        if(inventory instanceof ResourceInventory) resourceInventoryService.update((ResourceInventory)inventory);
        else if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
        else if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory)inventory);
    }

    public BaseInventory createBaseInventory(Base base){
        return baseInventoryService.create(base);
    }

    public ResourceInventory createResourceInventory(Base base){
        return resourceInventoryService.create(base);
    }

    public PlayerInventory createPlayerInventory(Player player){
        return playerInventoryService.create(player);
    }

    @Override
    public ItemInstance addItem(Inventory inventory, String templateId, long count){
        ItemInstance item = itemService.findFirstByInventoryAndTemplateId(inventory, templateId);

        if(item == null)
        {
            item = itemService.create(inventory, templateId, count);
            if(item == null) return null;
            inventory.getItems().add(item);

            if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
            else if(inventory instanceof ResourceInventory)
            {
                ((ResourceInventory) inventory).setLastRefresh(System.currentTimeMillis()); // TODO: refresh logic
                resourceInventoryService.update((ResourceInventory) inventory);
            }
            else if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory)inventory);
            else return null;

            return item;
        }

        if(inventory instanceof ResourceInventory)
        {
            // TODO: refresh logic
            ((ResourceInventory) inventory).setLastRefresh(System.currentTimeMillis());
            resourceInventoryService.update((ResourceInventory)inventory);
        }

        item.setCount(item.getCount()+count);
        itemService.update(item);
        return item;
    }

    public ItemInstance addItem(ItemInstance item, long amount){
        if (item.getInventory() instanceof ResourceInventory) {
            // TODO: refresh logic
            ((ResourceInventory) item.getInventory()).setLastRefresh(System.currentTimeMillis());
            resourceInventoryService.update((ResourceInventory) item.getInventory());
        }

        item.setCount(item.getCount()+amount);
        itemService.update(item);
        return item;
    }

    @Override
    public boolean consumeItem(ItemInstance item, long count) {
        if(item.getCount() - count >= 0){
            item.setCount(item.getCount() - count);
            itemService.update(item);
            return true;
        }
        return false;
    }

    @Override
    public boolean consumeItem(Inventory inventory, String id, long count) {
        final ItemInstance item = inventory.getItems().stream().filter(k -> k.getTemplateId().equals(id)).findFirst().orElse(null);
        return item != null && consumeItem(item, count);
    }

    public void deleteAll() {
        baseInventoryService.deleteAll();
        resourceInventoryService.deleteAll();
        playerInventoryService.deleteAll();
    }
}
