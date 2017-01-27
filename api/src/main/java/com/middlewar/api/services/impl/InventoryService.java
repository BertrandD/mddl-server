package com.middlewar.api.services.impl;

import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.interfaces.IInventoryService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.inventory.ItemContainer;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.api.services.ItemContainerService;
import com.middlewar.api.services.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class InventoryService implements IInventoryService {

    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BaseInventoryServiceImpl baseInventoryService;

    @Autowired
    private ItemContainerService itemContainerService;

    public synchronized void addResourceContainer(final Base base, final String templateId) {
        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.debug("addResourceContainer: template is null for item "+templateId);
            return;
        }

        logger.info("addResourceContainer: 0 "+templateId);
        final ItemContainer inventory = itemContainerService.create(base, templateId);
        if(inventory == null) {
            logger.debug("Cannot create resource inventory because returned null on creation !");
        }
    }

    public synchronized ItemInstance addItem(final ItemContainer container, final long amount) {
        refresh(container);

        final GameItem template = container.getItem().getTemplate();
        if(template == null) {
            logger.debug("addItem(Container, long): item template is null for container "+container.getId());
            return null;
        }

        if(amount == 0) {
            logger.debug("addItem(Container, long): Abort addItem " + template.getItemId() + " because amount is " + amount);
            return null;
        }

        double addcnt = amount;
        if(!container.canBeStored(amount, template.getVolume())) {
            addcnt = container.getAmountThatCanBeStored(template.getVolume());
            logger.debug("addItem(Container, long): amount has been edited because total amount > free capacity volume. new value is " + addcnt);
        }

        if(addcnt <= 0) {
            logger.debug("addItem(Container, long): x" + addcnt + " " + template.getItemId() + " not added ! amount is <= 0");
            return null;
        }

        container.getItem().addCount(addcnt);
        itemService.update(container.getItem());

        logger.debug("addItem(Container, long) [FINAL]: successfully make " + amount + " "+template.getItemId());
        return container.getItem();
    }

    @Override
    public synchronized ItemInstance addItem(final Inventory inventory, final String templateId, final long amount) {

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.debug("addItem(Inventory, String, long): template is null for item "+templateId);
            return null;
        }

        if(amount == 0) {
            logger.debug("addItem(Inventory, String, long): Abort addItem " + templateId + " because amount is " + amount);
            return null;
        }

        double addcnt = amount;
        if(!inventory.canBeStored(amount, template.getVolume())) {
            addcnt = inventory.getAmountThatCanBeStored(template.getVolume());
            logger.debug("addItem(Inventory, String, long): amount has been edited because total amount > free capacity volume. new value is " + addcnt);
        }

        if(addcnt <= 0) {
            logger.debug("addItem(Inventory, String, long): x" + addcnt + " " + templateId + " not added ! amount is <= 0");
            return null;
        }

        // TODO: repair
        // itemService.findOneBy((BaseInventory)inventory, templateId);
        ItemInstance item = null;
        if(item == null)
        {
            item = itemService.create(inventory, templateId, amount);
            if(item == null) {
                logger.debug("addItem(Inventory, String, long): Abort due to a problem with creation of ItemInstance with temple id = " + templateId);
                return null;
            }
            inventory.addItem(item);
            updateAsync(inventory);
        } else {
            item.addCount(amount);
            itemService.update(item);
        }

        logger.debug("addItem(Inventory, String, long) [FINAL]: successfully make " + amount + " "+templateId);
        return item;
    }

    @Override
    public synchronized boolean consumeItem(ItemInstance item, final long amount) {
        logger.debug("consumeItem(ItemInstance:"+item.getTemplateId()+", "+amount+")");

        if(item.getType().equals(ItemType.RESOURCE)) return consumeResource(item, amount);

        if(item.getCount() - amount >= 0) {
            logger.info("\t\t["+item.getCount()+" - "+amount+"] => ["+(item.getCount() - amount)+"]");
            item.removeCount(amount);

            if(item.getCount() == 0) {
                final IInventory inv = item.getInventory();
                ((Inventory)inv).getItems().remove(item.getTemplateId());
                update((Inventory)inv); // deleteAsync from inventory
                itemService.remove(item); // deleteAsync from items in mongo
            } else {
                itemService.update(item);
            }
            return true;
        }
        return false;
    }

    public synchronized boolean consumeResource(ItemInstance item, final long amount) {
        final ItemContainer container = (ItemContainer)item.getInventory();
        refresh(container);

        if(item.getCount() < amount) {
            logger.debug("Cannot consume resource " + item.getTemplateId() + " count " +amount + " because current count is minder");
            return false;
        }

        item.removeCount(amount);
        logger.info("consumeResource(ItemInstance, amount): -"+amount);
        itemContainerService.update(container);
        return true;
    }

    public synchronized void refresh(final Base base) {
        base.getResources().forEach(this::refresh);
    }

    public synchronized void refresh(final ItemContainer container) {
        final Base base = container.getBase();
        final long now = System.currentTimeMillis();
        final long last = container.getLastRefresh();
        final double prodPerHour = base.getBaseStat().getValue(container.getStat());
        final ItemInstance item = container.getItem();
        final GameItem template = item.getTemplate();

        // Security
        // Dont refresh if passed time is less than 1 second (1000 millis)
        if((now - last) <= 1000) {
            logger.debug("refresh: now - last <= 1000 ? Check possible spam");
            return;
        }

        if(prodPerHour <= 0 || template == null) {
            logger.debug("refresh: security problem");
            return;
        }

        // Auto-add calculation
        // (amount per second) * (time without refreshing)
        double add = (((prodPerHour / 3600) * ((now - last) / 1000)));

        logger.info("refresh: Trying add " + add + " " + template.getItemId());

        if(add > 0 && !container.canBeStored(add, template.getVolume())) {
            add = container.getAmountThatCanBeStored(template.getVolume());
            logger.info("\tCan't be added (current capacity is " + container.getVolume() + "/" + container.getMaxVolume() + "), recalculating amount that can be stored : " + add);
        }

        if(add <= 0) {
            // if caused by a full container: update lastrefresh to now, else return.
            if(container.getFreeVolume() < template.getVolume()) {
                container.setLastRefresh(now);
                itemContainerService.update(container);
                return;
            }
            logger.info("addItem " + template.getItemId() + " not added because there is container place to store him... Its not an error :)");
            logger.info("\t(current capacity is " + container.getVolume() + "/" + container.getMaxVolume() + ")");
            return;
        }

        item.addCount(add);
        container.setLastRefresh(now);
        itemService.update(item);
        itemContainerService.update(container);
    }

    public void updateAsync(Inventory inventory) {
        if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
        else playerInventoryService.updateAsync((PlayerInventory)inventory);
    }

    public void update(Inventory inventory) {
        if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory) inventory);
        else playerInventoryService.update((PlayerInventory)inventory);
    }
}
