package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.interfaces.IInventoryService;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.items.GameItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author LEBOC Philippe
 */
@Service
public class InventoryService implements IInventoryService {

    private final Logger logger = Logger.getLogger(InventoryService.class.getName());

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BaseInventoryService baseInventoryService;

    public void updateAsync(Inventory inventory) {
        if(inventory instanceof BaseInventory) baseInventoryService.updateAsync((BaseInventory) inventory);
        else if(inventory instanceof PlayerInventory) playerInventoryService.updateAsync((PlayerInventory) inventory);
    }

    public void update(Inventory inventory) {
        if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
        else if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory)inventory);
    }

    /**
     * Refreshing resources
     * @param base
     */
    public synchronized void refreshResource(Base base) {
        logger.info("Refreshing resources...");
        final BaseInventory baseInventory = base.getBaseInventory();
        final HashMap<String, Double> production = base.getProduction();
        final long now = System.currentTimeMillis();

        if(production == null) {
            logger.info("Abort refreshing resources because : Empty production");
            baseInventory.setLastRefresh(now);
            update(baseInventory);
            return;
        }

        // Add resources
        production.forEach((k,v) ->
        {
            final ItemInstance inst = baseInventory.getItems().stream().filter(j -> j.getTemplateId().equalsIgnoreCase(k)).findFirst().orElse(null);
            double value = (v / 3600) * ((now - baseInventory.getLastRefresh()) / 1000);
            logger.info("Trying to add " + value + " to " + k);
            if(value > 0) {
                if(inst == null) {
                    logger.warning("Item " + k + " doesn't exist. It must be created on his building creation. Aborting "+k+" refresh...");
                } else {
                    value = baseInventory.getHowManyCanBeAdded(value, inst.getTemplate().getVolume());
                    inst.addCount(value);
                    logger.info("\tadded + " + value + " to " + inst.getTemplateId());
                    itemService.updateAsync(inst);
                }
            }
        });

        baseInventory.setLastRefresh(now);
        update(baseInventory);
    }

    /**
     * Used by BuildingTaskManager to create items corresponding to the buildings (Extractors) when creation has ended.
     * @param inventory
     * @param templateId
     * @param amount
     */
    public synchronized void addVoidItem(final Inventory inventory, final String templateId, long amount) {
        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.warning("addItem: template is null for item "+templateId);
            return;
        }

        logger.info("addItem: " + amount + " "+templateId);
        final ItemInstance existingItem = itemService.findOneBy(inventory, templateId);
        if(existingItem != null) {
            logger.warning("Trying to add void item but item already exist ! Aborting...");
            return;
        }

        final ItemInstance item = itemService.create(inventory, templateId, amount);
        if(item == null) {
            logger.warning("Cannot create void item because itemService returned null item on creation !");
            return;
        }
        inventory.getItems().add(item);
        updateAsync(inventory);
    }

    /**
     * Used to add a NON RESOURCE item (because resources are Double and others are Long)
     * @see #refreshResource to see how resources are automatically added
     * @param inventory
     * @param templateId
     * @param amount
     * @return instance of item newly created
     */
    @Override
    public synchronized ItemInstance addItem(final Inventory inventory, final String templateId, long amount) {

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.warning("addItem(Inventory, String, long): template is null for item "+templateId);
            return null;
        }

        amount = inventory.getHowManyCanBeAdded(amount, template.getVolume());
        if(amount == 0) {
            logger.warning("Abort addItem " + templateId + " because amount that can be added is " + amount);
            return null;
        }

        logger.info("addItem: " + amount + " "+templateId);
        ItemInstance item = itemService.findOneBy(inventory, templateId);
        if(item == null)
        {
            item = itemService.create(inventory, templateId, amount);
            if(item == null) return null;
            inventory.getItems().add(item);

            updateAsync(inventory);
            return item;
        }

        item.addCount(amount);
        itemService.updateAsync(item);
        return item;
    }

    /**
     * @param item that is already in inventory. It must be get from Inventory and NOT new Instance creation !
     * @param amount that must be added to the current existing item
     * @return item with new value, null if amount cant be added or if item doesnt exist in an inventory
     */
    @Override
    public synchronized ItemInstance addItem(ItemInstance item, long amount) {
        final GameItem template = item.getTemplate();
        if(template == null) {
            logger.warning("addItem(ItemInstance, long): template is null for item "+ item.getTemplateId());
            return null;
        }

        final String templateId = item.getTemplateId();
        final Inventory inventory = item.getInventory();

        amount = inventory.getHowManyCanBeAdded(amount, template.getVolume());
        if(amount == 0) {
            logger.warning("Abort addItem " + templateId + " because amount that can be added is " + amount);
            return null;
        }

        logger.info("addItem: " + amount + " "+templateId);
        ItemInstance existingOne = itemService.findOneBy(inventory, templateId);
        if(existingOne == null) {
            logger.warning("Bad usage of addItem(ItemInstance, long) ! Read Doc !");
            return null;
        }

        item.addCount(amount);
        itemService.updateAsync(item);
        return item;
    }

    @Override
    public synchronized boolean consumeItem(ItemInstance item, final long amount) {
        logger.info("consumeItem(ItemInstance:"+item.getTemplateId()+", "+amount+")");
        if(item.getType().equals(ItemType.RESOURCE)) {
            final BaseInventory resources = (BaseInventory)item.getInventory();
            refreshResource(resources.getBase());
            final String itemTemplateId = item.getTemplateId();
            item = resources.getItems().stream().filter(k->k.getTemplateId().equals(itemTemplateId)).findFirst().orElse(null);
        }

        if(item.getCount() - amount >= 0) {
            logger.info("\t\t["+item.getCount()+" - "+amount+"] => ["+(item.getCount() - amount)+"]");
            item.setCount(item.getCount() - amount);

            if(item.getCount() == 0 && !item.getType().equals(ItemType.RESOURCE)) {
                final Inventory inv = item.getInventory();
                inv.getItems().remove(item);
                update(inv); // delete from inventory
                itemService.delete(item.getId()); // delete from items in mongo
            } else {
                itemService.update(item);
            }

            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean consumeItem(Inventory inventory, String id, final long amount) {
        logger.info("consumeItem(Inventory:"+inventory.getClass().getSimpleName()+", "+id+", "+amount+")");
        final ItemInstance item = inventory.getItems().stream().filter(k -> k.getTemplateId().equals(id)).findFirst().orElse(null);
        return item != null && consumeItem(item, amount);
    }
}
