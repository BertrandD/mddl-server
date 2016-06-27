package com.gameserver.services;

import com.config.Config;
import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.BuildingCategory;
import com.gameserver.enums.ItemType;
import com.gameserver.interfaces.IInventoryService;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.model.buildings.Extractor;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public void updateAsync(Inventory inventory){
        if(inventory instanceof BaseInventory) baseInventoryService.updateAsync((BaseInventory) inventory);
        else if(inventory instanceof PlayerInventory) playerInventoryService.updateAsync((PlayerInventory) inventory);
    }

    public void update(Inventory inventory){
        if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory)inventory);
        else if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory)inventory);
    }

    public BaseInventory createBaseInventory(Base base){
        return baseInventoryService.create(base);
    }
    public PlayerInventory createPlayerInventory(Player player){
        return playerInventoryService.create(player);
    }

    /**
     * Refreshing resources
     * @param base
     */
    public synchronized void refreshResource(Base base){
        logger.info("Refreshing resources...");
        final BaseInventory baseInventory = base.getBaseInventory();
        final List<BuildingInstance> extractors = base.getBuildings().stream().filter(k ->
            k != null &&
            k.getTemplate().getType().equals(BuildingCategory.Extractor) &&
            k.getCurrentLevel() > 0)
        .collect(Collectors.toList());

        final long now = System.currentTimeMillis();
        final HashMap<String, Double> generatedResources = new HashMap<>();

        if(extractors.isEmpty()) return;

        for (BuildingInstance extractor : extractors)
        {
            final Extractor extractorTemplate = (Extractor)BuildingData.getInstance().getBuilding(extractor.getBuildingId());
            for (GameItem gameItem : extractorTemplate.getProduceItems())
            {
                final ItemInstance resource = baseInventory.getItems().stream().filter(k -> k != null && k.getTemplateId().equals(gameItem.getItemId())).findFirst().orElse(null);
                if(resource != null)
                {
                    final double productionCnt = ((((double) extractorTemplate.getProductionAtLevel(gameItem.getItemId(), extractor.getCurrentLevel()) / 3600)) * ((now - baseInventory.getLastRefresh()) / 1000) * Config.RESOURCE_PRODUCTION_MODIFIER);
                    if(generatedResources.containsKey(gameItem.getItemId()))
                    {
                        final double containedResource = generatedResources.get(gameItem.getItemId());
                        generatedResources.replace(gameItem.getItemId(), containedResource+productionCnt);
                    } else generatedResources.put(gameItem.getItemId(), productionCnt);
                }
                else
                {
                    generatedResources.put(gameItem.getItemId(), 0.0);
                }
            }
        }

        // Apply resources modules bonus
        for (BuildingInstance extractor : extractors){
            for(Module module : extractor.getModules()){
                Double resource = generatedResources.get(module.getAffected());
                if(resource != null) {
                    resource = (resource * module.getMultiplicator()); // here is applyied module multiplicator
                    generatedResources.replace(module.getAffected(), resource);
                }
            }
        }

        for (String itemId : generatedResources.keySet())
        {
            ItemInstance item = baseInventory.getItems().stream().filter(k -> k != null && k.getTemplateId().equals(itemId)).findFirst().orElse(null);
            if(item == null) {
                item = itemService.create(baseInventory, itemId, 0);
                if(generatedResources.get(itemId) > 0)
                    item = addResource(item, generatedResources.get(itemId));
                baseInventory.getItems().add(item);
            } else {
                if(generatedResources.get(itemId) > 0)
                    addResource(item, generatedResources.get(item.getTemplateId()));
            }
        }

        baseInventory.setLastRefresh(now);
        updateAsync(baseInventory);
    }

    /**
     * Method created and used only by refreshResource to avoid infinite loop from refreshResource() <=> addItem()
     * @param item to be updated
     * @param amount to be added
     * @return item with current amount + added amount
     */
    private synchronized ItemInstance addResource(ItemInstance item, final double amount) {
        final GameItem template = item.getTemplate();
        if(template == null) return null;

        final long amountThatCanBeAdded = Math.floorDiv(item.getInventory().getFreeVolume(), template.getVolume());
        if (amountThatCanBeAdded > 0) {
            item.setCount(item.getCount()+Math.min(amountThatCanBeAdded, amount));
            logger.info("\t\taddResource("+item.getTemplateId()+" [+"+Math.min(amountThatCanBeAdded, amount)+"], "+item.getCount()+")");
            itemService.update(item);
        }

        return item;
    }

    @Override
    public synchronized ItemInstance addItem(Inventory inventory, String templateId, long amount) {
        ItemInstance item = itemService.findFirstByInventoryAndTemplateId(inventory, templateId);

        final GameItem template = ItemData.getInstance().getTemplate(templateId);
        if(template == null) {
            logger.warning("addItem: template is null for item "+templateId);
            return null;
        }

        if(inventory.getFreeVolume() < (template.getVolume() * amount)) {
            if(inventory.getFreeVolume() > 0 && inventory.getFreeVolume() >= template.getVolume())
                amount = (inventory.getFreeVolume() / template.getVolume());
            else {
                logger.warning("addItem: cannot add more item to inventory.");
                return null;
            }
        }

        logger.info("addItem: " + amount + " "+templateId);

        if(item == null)
        {
            item = itemService.create(inventory, templateId, amount);
            if(item == null) return null;
            inventory.getItems().add(item);

            if(inventory instanceof BaseInventory) baseInventoryService.update((BaseInventory) inventory);
            else if(inventory instanceof PlayerInventory) playerInventoryService.update((PlayerInventory) inventory);
            else return null;

            return item;
        }

        if(item.getType().equals(ItemType.RESOURCE)) {
            refreshResource(((BaseInventory)item.getInventory()).getBase());
            item = inventory.getItems().stream().filter(k -> k != null && k.getTemplateId().equals(templateId)).findFirst().orElse(null);
        }

        item.setCount(item.getCount()+amount);
        itemService.updateAsync(item);
        return item;
    }

    @Override
    public synchronized ItemInstance addItem(ItemInstance item, long amount) {

        final GameItem template = item.getTemplate();
        if(template == null) return null;

        final String itemTemplateId = item.getTemplateId();
        final Inventory inventory = item.getInventory();

        if(inventory.getFreeVolume() < (template.getVolume() * amount)) {
            if(inventory.getFreeVolume() > 0 && inventory.getFreeVolume() >= template.getVolume())
                amount = (inventory.getFreeVolume() / template.getVolume());
            else {
                logger.warning("addItem: cannot add more item to inventory.");
                return null;
            }
        }

        logger.info("addItem: " + amount + " "+itemTemplateId);

        if(item.getType().equals(ItemType.RESOURCE)) {
            refreshResource(((BaseInventory)inventory).getBase());
            item = inventory.getItems().stream().filter(k -> k != null && k.getTemplateId().equals(itemTemplateId)).findFirst().orElse(null);
        }

        item.setCount(item.getCount()+amount);
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

    public void deleteAll() {
        baseInventoryService.deleteAll();
        playerInventoryService.deleteAll();
    }
}
