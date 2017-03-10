package com.middlewar.api.services.impl;

import com.middlewar.api.services.BaseInventoryService;
import com.middlewar.api.services.PlayerInventoryService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.interfaces.IInventory;
import com.middlewar.core.interfaces.IInventoryService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.*;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.api.services.ResourceService;
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
    private BaseInventoryService baseInventoryService;

    //@Autowired
    //private FleetInventoryService fleetInventoryService;

    @Autowired
    private ResourceService resourceService;

    @Override
    public synchronized Resource createNewResource(final Base base, final String templateId) {
        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) return null;
        return resourceService.create(base, templateId);
    }

    @Override
    public synchronized ItemInstance addItem(final IInventory inventory, final String templateId, final long amount) {

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) return null;

        if(amount <= 0) return null;

        long addcnt = amount;
        if(!canBeStored(inventory, template.getVolume(), amount)) {
            final long storableAmount = getStorableAmount(inventory, template.getVolume());
            addcnt = storableAmount == -1 ? addcnt : storableAmount;
        }

        if(addcnt <= 0) return null;

        final ItemInstance item = inventory.getItem(templateId);
        if(item == null) return itemService.create(inventory, templateId, addcnt);

        item.addCount(addcnt);
        itemService.update(item);

        return item;
    }

    @Override
    public synchronized boolean addResource(Resource resource, long amount) {

        if(amount <= 0) return false;

        final GameItem template = resource.getItem().getTemplate();

        if(resource.getAvailableCapacity() < template.getVolume() * amount) {
            amount = resource.getAvailableCapacity() / template.getVolume();
        }

        if(amount <= 0) return false;

        resource.getItem().addCount(amount);
        itemService.update(resource.getItem());

        return true;
    }

    @Override
    public synchronized boolean consumeItem(ItemInstance item, final long amount) {

        if(item.getCount() - amount < 0) return false;

        item.removeCount(amount);

        if(item.getCount() == 0) {
            final Inventory inventory = (Inventory) item.getInventory();
            inventory.getItems().remove(item);
            update(inventory);
            itemService.remove(item);
        } else {
            itemService.update(item);
        }

        return true;
    }

    @Override
    public synchronized boolean consumeResource(Resource resource, final long amount) {

        refresh(resource);

        if(amount <= 0) return false;

        final ItemInstance item = resource.getItem();

        if(item.getCount() < amount) return false;

        item.removeCount(amount);

        resourceService.update(resource);
        return true;
    }

    public synchronized void refresh(final Base base) {
        if(base == null) return;
        base.getResources().forEach(this::refresh);
    }

    public synchronized void refresh(final Resource resource) {
        final Base base = resource.getBase();
        final long now = System.currentTimeMillis();
        final long last = resource.getLastRefresh();
        final double prodPerHour = base.getBaseStat().getValue(resource.getStat());
        final ItemInstance item = resource.getItem();
        final GameItem template = item.getTemplate();

        if(template == null || prodPerHour <= 0) return;

        // Calculation
        // (amount per second) * (time without refreshing)
        double profAmountPerSecond = (prodPerHour / 3600);
        double elapsedTimeInSecond = (now - last) / 1000;
        long add = (long)(profAmountPerSecond * elapsedTimeInSecond);

        if(add <= 0) return;

        if(resource.getAvailableCapacity() < template.getVolume() * add) {
            add = resource.getAvailableCapacity() / template.getVolume();
        }

        if(add <= 0) return;

        item.addCount(add);

        resource.setLastRefresh(System.currentTimeMillis());
        resourceService.update(resource);
    }

    private long getStorableAmount(IInventory inventory, long volume) {
        return (inventory.getAvailableCapacity() / volume); // TODO: check division
    }

    private boolean canBeStored(final IInventory inventory, long volume, final long amount) {
        return inventory.getAvailableCapacity() > (volume * amount);
    }

    public void update(IInventory inventory) {
        if(inventory instanceof BaseInventory) update((BaseInventory) inventory);
        else if(inventory instanceof FleetInventory) update((FleetInventory) inventory);
        else if(inventory instanceof PlayerInventory) update((PlayerInventory)inventory);
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
        //fleetInventoryService.update(inventory);
    }
}
