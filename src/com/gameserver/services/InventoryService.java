package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.interfaces.IInventory;
import com.gameserver.interfaces.IInventoryService;
import com.gameserver.model.Base;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.AbstractMultiStorageInventory;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.model.items.GameItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ResourceInventoryService resourceInventoryService;

    public synchronized void addResourceInventory(final Base base, final String templateId) {
        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.warning("addResourceInventory: template is null for item "+templateId);
            return;
        }

        logger.info("addResourceInventory: 0 "+templateId);
        final ResourceInventory inventory = resourceInventoryService.create(base, templateId);
        if(inventory == null) {
            logger.warning("Cannot create resource inventory because returned null on creation !");
        }
    }

    @Override
    public synchronized ItemInstance addItem(final AbstractMultiStorageInventory inventory, final String templateId, final long amount) {

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.warning("addItem(Inventory, String, long): template is null for item "+templateId);
            return null;
        }

        if(amount == 0) {
            logger.warning("Abort addItem " + templateId + " because amount that can be added is " + amount);
            return null;
        }

        double addcnt = amount;
        if(!inventory.canBeStored(amount, template.getVolume())) {
            addcnt = inventory.getAmountThatCanBeStored(template.getVolume());
        }

        if(addcnt == 0) {
            logger.info("addItem " + templateId + " not added because there is inventory place to store him... Its not an error :)");
            return null;
        }

        ItemInstance item = itemService.findOneBy((BaseInventory)inventory, templateId);

        if(item == null) {
            item = itemService.create(inventory, templateId, amount);
            if(item == null) {
                logger.warning("Abort due to a problem with creation of ItemInstance with temple id = " + templateId);
                return null;
            }
            inventory.addItem(item);
            updateAsync(inventory);
        } else {
            item.addCount(amount);
            itemService.updateAsync(item);
        }

        logger.info("addItem: " + amount + " "+templateId);
        return item;
    }

    @Override
    public synchronized boolean consumeItem(ItemInstance item, final long amount) {
        logger.info("consumeItem(ItemInstance:"+item.getTemplateId()+", "+amount+")");

        if(item.getType().equals(ItemType.RESOURCE)) return consumeResource(item, amount);

        if(item.getCount() - amount >= 0) {
            logger.info("\t\t["+item.getCount()+" - "+amount+"] => ["+(item.getCount() - amount)+"]");
            item.removeCount(amount);

            if(item.getCount() == 0) {
                final IInventory inv = item.getInventory();
                ((AbstractMultiStorageInventory)inv).getItems().remove(item);
                update((AbstractMultiStorageInventory)inv); // deleteAsync from inventory
                itemService.deleteAsync(item.getId()); // deleteAsync from items in mongo
            } else {
                itemService.update(item);
            }

            return true;
        }
        return false;
    }

    public synchronized boolean consumeResource(ItemInstance item, final long amount) {
        final ResourceInventory inventory = (ResourceInventory)item.getInventory();
        refresh(inventory);

        if(item.getCount() < amount) {
            logger.warning("Cannot consume resource " + item.getTemplateId() + " count " +amount + " because current count is minder");
            return false;
        }

        item.removeCount(amount);
        logger.info("consumeResource(ItemInstance, amount): -"+amount);
        resourceInventoryService.update(inventory);
        return true;
    }

    public synchronized void refresh(final Base base) {
        base.getResources().forEach(this::refresh);
    }

    public synchronized void refresh(final ResourceInventory inventory) {
        final Base base = inventory.getBase();
        final long now = System.currentTimeMillis();
        final long last = inventory.getLastRefresh();
        final double prodPerHour = base.getBaseStat().getValue(inventory.getStat());
        final ItemInstance item = inventory.getItem();
        final GameItem template = item.getTemplate();

        // Security
        if((now - last) <= 0) return;

        // Auto-add calculation
        double add = (((prodPerHour / 3600) * ((now - last) / 1000)));


        logger.info("Trying to add " + add + " " + template.getItemId());

        if(add != 0 && !inventory.canBeStored(add, template.getVolume())) {
            add = inventory.getAmountThatCanBeStored(template.getVolume());
            logger.info("\tCan't be added (current capacity is " + inventory.getVolume() + "/" + inventory.getMaxVolume() + "), recalculating amount that can be stored : " + add);
        }

        if(add == 0) {
            // if caused by a full inventory: update lastrefresh to now, else simply return.
            if(inventory.getFreeVolume() < template.getVolume()) {
                inventory.setLastRefresh(now);
                resourceInventoryService.update(inventory);
                return;
            }
            logger.info("addItem " + template.getItemId() + " not added because there is inventory place to store him... Its not an error :)");
            logger.info("\t(current capacity is " + inventory.getVolume() + "/" + inventory.getMaxVolume() + ")");
            return;
        }

        item.addCount(add);
        inventory.setLastRefresh(now);
        itemService.updateAsync(item);
        resourceInventoryService.update(inventory);
    }

    public void updateAsync(AbstractMultiStorageInventory inventory) {
        if(inventory instanceof BaseInventory) baseInventoryService.updateAsync((BaseInventory)inventory);
        else playerInventoryService.updateAsync((PlayerInventory)inventory);
    }

    public void update(AbstractMultiStorageInventory inventory) {
        if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory) inventory);
        else playerInventoryService.update((PlayerInventory)inventory);
    }
}
