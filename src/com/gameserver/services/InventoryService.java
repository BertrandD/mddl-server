package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.interfaces.IInventoryService;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.buildings.Mine;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.model.items.GameItem;
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

    @Autowired
    private ResourceInventoryService resourceInventoryService;

    private static final String MINE_METAL = "mine_metal";
    private static final String METAL = "metal";

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

    public ItemInstance refreshResource(Base base){
        ItemInstance item = null;
        final BuildingInstance mineMetal = base.getBuildings().stream().filter(k->k.getBuildingId().equals(MINE_METAL)).findFirst().orElse(null);
        if(mineMetal != null && mineMetal.getCurrentLevel() > 0)
        {
            final ResourceInventory resources = base.getResources();
            ItemInstance metal = resources.getItems().stream().filter(k->k.getTemplateId().equals(METAL)).findFirst().orElse(null);
            if(metal != null)
            {
                final long now = System.currentTimeMillis();
                final Mine template = (Mine) mineMetal.getTemplate();
                final long amount = (long) ((((float)template.getProduction(mineMetal.getCurrentLevel()) / 3600)) * ((now - resources.getLastRefresh()) / 1000));

                if(amount > 0)
                {
                    item = addResource(metal, amount);
                }
            }
        }
        return item;
    }

    /**
     * Method created and used only by refreshResource to avoid infinite loop from refreshResource() <=> addItem()
     * @param item to be updated
     * @param amount to be added
     * @return item with current amount + added amount
     */
    private ItemInstance addResource(ItemInstance item, final long amount) {
        final GameItem template = item.getTemplate();
        if(template == null) return null;

        final long amountThatCanBeAdded = Math.floorDiv(item.getInventory().getFreeVolume(), template.getVolume());
        if (amountThatCanBeAdded < 1) {
            return null;
        }

        item.setCount(item.getCount() + Math.min(amountThatCanBeAdded, amount));
//        itemService.update(item);
        return item;
    }

    @Override
    public ItemInstance addItem(Inventory inventory, String templateId, final long amount){
        ItemInstance item = itemService.findFirstByInventoryAndTemplateId(inventory, templateId);

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) return null;

        if(inventory.getFreeVolume() < (template.getVolume()*amount)) return null;

        if(item == null)
        {
            item = itemService.create(inventory, templateId, amount);
            if(item == null) return null;
            inventory.getItems().add(item);

            if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
            else if(inventory instanceof ResourceInventory) resourceInventoryService.update((ResourceInventory) inventory);
            else if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory)inventory);
            else return null;

            return item;
        }

        if(item.getType().equals(ItemType.RESOURCE)){
            final ItemInstance refreshed = refreshResource(((ResourceInventory)item.getInventory()).getBase());
            if(refreshed != null)
                item = refreshed;
        }

        item.setCount(item.getCount()+amount);
        itemService.update(item);
        return item;
    }

    @Override
    public ItemInstance addItem(ItemInstance item, final long amount) {

        final GameItem template = item.getTemplate();
        if(template == null) return null;

        if(item.getInventory().getFreeVolume() < (template.getVolume()*amount)) return null;

        if(item.getType().equals(ItemType.RESOURCE)){
            ItemInstance refreshed = refreshResource(((ResourceInventory)item.getInventory()).getBase());
            if(refreshed != null)
                item = refreshed;
        }

        item.setCount(item.getCount()+amount);
        itemService.update(item);
        return item;
    }

    @Override
    public boolean consumeItem(ItemInstance item, final long amount) {
        if(item.getType().equals(ItemType.RESOURCE)){
            ItemInstance refreshed = refreshResource(((ResourceInventory)item.getInventory()).getBase());
            if(refreshed != null)
                item = refreshed;
        }

        if(item.getCount() - amount >= 0){
            item.setCount(item.getCount() - amount);
            itemService.update(item);
            return true;
        }
        return false;
    }

    @Override
    public boolean consumeItem(Inventory inventory, String id, final long amount) {
        final ItemInstance item = inventory.getItems().stream().filter(k -> k.getTemplateId().equals(id)).findFirst().orElse(null);
        return item != null && consumeItem(item, amount);
    }

    public void deleteAll() {
        baseInventoryService.deleteAll();
        resourceInventoryService.deleteAll();
        playerInventoryService.deleteAll();
    }
}
