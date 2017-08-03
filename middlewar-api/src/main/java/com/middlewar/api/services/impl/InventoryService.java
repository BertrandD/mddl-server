package com.middlewar.api.services.impl;

import com.middlewar.api.services.BaseInventoryService;
import com.middlewar.api.services.ItemService;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.api.services.ResourceService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.interfaces.IInventoryService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.FleetInventory;
import com.middlewar.core.model.inventory.Inventory;
import com.middlewar.core.model.inventory.PlayerInventory;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
@Slf4j
public class InventoryService implements IInventoryService {

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BaseInventoryService baseInventoryService;

    //@Autowired
    //private FleetInventoryService fleetInventoryService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public synchronized Resource createNewResource(final Base base, final String templateId) {
        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if (template == null) return null;
        return resourceService.create(base, templateId);
    }

    @Override
    public synchronized ItemInstance addItem(final Inventory inventory, final String templateId, final long amount) {

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if (template == null) {
            log.info("Template " + templateId + " nod found");
            return null;
        }

        if (amount <= 0) {
            log.info("Not allowed to add a null or negative amount of " + templateId + ". Given " + amount);
            return null;
        }

        long addcnt = amount;
        if (!canBeStored(inventory, template.getVolume(), amount)) {
            final long storableAmount = getStorableAmount(inventory, template.getVolume());
            addcnt = storableAmount == -1 ? addcnt : storableAmount;
        }

        if (addcnt <= 0) {
            log.info("Item " + templateId + " cannot be stored");
            return null;
        }

        final ItemInstance item = inventory.getItem(templateId);
        if (item == null) {
            log.debug(templateId + " not found in inventory. Item created.");
            return itemService.create(inventory, templateId, addcnt);
        }

        item.addCount(addcnt);
        itemService.update(item);

        return item;
    }

    @Override
    public synchronized boolean addResource(Resource resource, long amount) {

        if (amount <= 0) return false;

        final GameItem template = resource.getItem().getTemplate();

        // Capacity of -1 == INFINITY
        final long capacity = resource.getAvailableCapacity();
        if (capacity != -1 && capacity < template.getVolume() * amount) {
            amount = capacity / template.getVolume();
        }

        if (amount <= 0) return false;

        resource.getItem().addCount(amount);
        itemService.update(resource.getItem());

        return true;
    }

    @Override
    public synchronized boolean consumeItem(ItemInstance item, final long amount) {

        if (item.getCount() - amount < 0) return false;

        item.removeCount(amount);

        if (item.getCount() == 0) {
            final Inventory inventory = (Inventory) item.getInventory();
            inventory.getItems().remove(item);
            update(inventory);
            itemService.delete(item);
        } else {
            itemService.update(item);
        }

        return true;
    }

//    24/07/2017 : code used only in tests, so commented because seems to be useless. Delete it later if it's still useless !
//    @Override
//    public synchronized boolean consumeResource(Resource resource, final long amount) {
//
//        refreshResources(resource);
//
//        if(amount <= 0) return false;
//
//        final ItemInstance item = resource.getItem();
//
//        if(item.getCount() < amount) return false;
//
//        item.removeCount(amount);
//
//        resourceService.update(resource);
//        return true;
//    }

    public synchronized void refreshResources(final Base base) {
        if (base == null) return;
        base.getResources().forEach(this::refreshResources);
    }

    public synchronized void refreshResources(final Resource resource) {
        final long now = TimeUtil.getCurrentTime();
        final long last = resource.getLastRefresh();
        final double prodPerHour = resource.getProdPerHour();
        final ItemInstance item = resource.getItem();
        final GameItem template = item.getTemplate();

        if (template == null || prodPerHour <= 0) return;

        // Calculation
        // (amount per second) * (time without refreshing)
        final double profAmountPerSecond = (prodPerHour / 3600);
        final double elapsedTimeInSecond = (now - last) / 1000;
        long add = (long) (profAmountPerSecond * elapsedTimeInSecond);

        if (addResource(resource, add)) {
            resource.setLastRefresh(now);
        }
    }

    private long getStorableAmount(IInventory inventory, long volume) {
        if (volume == 0) {
            return -1;
        }
        return (inventory.getAvailableCapacity() / volume);
    }

    private boolean canBeStored(final IInventory inventory, long volume, final long amount) {
        return inventory.getAvailableCapacity() > (volume * amount);
    }

    public void update(IInventory inventory) {
        if (inventory instanceof BaseInventory) update((BaseInventory) inventory);
        else if (inventory instanceof FleetInventory) update((FleetInventory) inventory);
        else if (inventory instanceof PlayerInventory) update((PlayerInventory) inventory);
    }

    public void update(Resource inventory) {
        resourceService.update(inventory);
    }

    public void update(PlayerInventory inventory) {
        playerInventoryService.update(inventory);
    }

    public void update(BaseInventory inventory) {
        baseInventoryService.update(inventory);
    }

    public void update(FleetInventory inventory) {
        // TODO:
        //fleetInventoryService.update(inventory);
    }
}
