package com.gameserver.services;

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
import com.gameserver.model.inventory.ResourceInventory;
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

    public void refreshResource(Base base){
        logger.info("Refreshing resources...");
        final ResourceInventory baseResourcesInventory = base.getResourcesInventory();
        final List<BuildingInstance> extractors = base.getBuildings().stream().filter(
                k->k.getTemplate().getType().equals(BuildingCategory.Extractor) &&
                k.getCurrentLevel() > 0).collect(Collectors.toList());

        //final Extractor extractorTemplate = (Extractor)BuildingData.getInstance().getBuilding("mine");

        final long now = System.currentTimeMillis();
        final HashMap<String, Long> generatedResources = new HashMap<>();

        if(extractors.isEmpty()) return;

        for (BuildingInstance extractor : extractors)
        {
            final Extractor extractorTemplate =  (Extractor)BuildingData.getInstance().getBuilding(extractor.getBuildingId());
            logger.info(extractorTemplate.getName()+" : "+extractorTemplate.getProduceItems().isEmpty());

            for (GameItem gameItem : extractorTemplate.getProduceItems())
            {
                final ItemInstance resourceToUpdate = baseResourcesInventory.getItems().stream().filter(k->k.getTemplateId().equals(gameItem.getItemId())).findFirst().orElse(null);
                if(resourceToUpdate != null){
                    final long productionCnt = (long)((((float) extractorTemplate.getProductionAtLevel(gameItem.getItemId(), extractor.getCurrentLevel()) / 3600)) * ((now - resourceToUpdate.getLastRefresh()) / 1000));
                    if(generatedResources.containsKey(gameItem.getItemId())){
                        final long containedResource = generatedResources.get(gameItem.getItemId());
                        generatedResources.replace(gameItem.getItemId(), containedResource+productionCnt);
                    } else generatedResources.put(gameItem.getItemId(), productionCnt);
                }else{
                    generatedResources.put(gameItem.getItemId(), 0L);
                }
            }
        }

        // Apply resources modules bonus
        for (BuildingInstance extractor : extractors){
            for(Module module : extractor.getModules()){
                Long resource = generatedResources.get(module.getAffected());
                if(resource != null) {
                    resource = (long)(resource * module.getMultiplicator()); // here is applyied module multiplicator
                    generatedResources.replace(module.getAffected(), resource);
                }
            }
        }

        for (String itemId : generatedResources.keySet())
        {
            ItemInstance item = baseResourcesInventory.getItems().stream().filter(k->k.getTemplateId().equals(itemId)).findFirst().orElse(null);
            if(item == null){
                item = itemService.create(baseResourcesInventory, itemId, 0);
                if(generatedResources.get(itemId) > 0)
                    item = addResource(item, generatedResources.get(itemId));
                baseResourcesInventory.getItems().add(item);
            } else {
                if(generatedResources.get(itemId) > 0)
                    addResource(item, generatedResources.get(item.getTemplateId()));
            }
        }

        update(baseResourcesInventory);
    }

    /**
     * Method created and used only by refreshResource to avoid infinite loop from refreshResource() <=> addItem()
     * @param item to be updated
     * @param amount to be added
     * @return item with current amount + added amount
     */
    private ItemInstance addResource(ItemInstance item, final long amount) {
        logger.info("\t\taddResource("+item.getTemplateId()+" ["+item.getCount()+"], +"+amount+")");
        final GameItem template = item.getTemplate();
        if(template == null) return null;

        final long amountThatCanBeAdded = Math.floorDiv(item.getInventory().getFreeVolume(), template.getVolume());
        if (amountThatCanBeAdded > 0) {
            item.setCount(item.getCount()+Math.min(amountThatCanBeAdded, amount));
            item.setLastRefresh(System.currentTimeMillis());
            itemService.update(item);
        }

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
            refreshResource(((ResourceInventory)item.getInventory()).getBase());
            item = itemService.findOne(item.getId());
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
            refreshResource(((ResourceInventory)item.getInventory()).getBase());
            item = itemService.findOne(item.getId());
        }

        item.setCount(item.getCount()+amount);
        itemService.update(item);
        return item;
    }

    @Override
    public boolean consumeItem(ItemInstance item, final long amount) {
        logger.info("consumeItem(ItemInstance:"+item.getTemplateId()+", "+amount+")");
        if(item.getType().equals(ItemType.RESOURCE)){
            final ResourceInventory resources = (ResourceInventory)item.getInventory();
            refreshResource(resources.getBase());
            item = itemService.findOne(item.getId());
        }

        if(item.getCount() - amount >= 0){
            logger.info("\t\t["+item.getCount()+" - "+amount+"] => ["+(item.getCount()-amount)+"]");
            item.setCount(item.getCount() - amount);

            if(item.getCount() == 0){
                itemService.delete(item.getId());
            }else{
                itemService.update(item);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean consumeItem(Inventory inventory, String id, final long amount) {
        logger.info("consumeItem(Inventory:"+inventory.getClass().getSimpleName()+", "+id+", "+amount+")");
        final ItemInstance item = inventory.getItems().stream().filter(k -> k.getTemplateId().equals(id)).findFirst().orElse(null);
        return item != null && consumeItem(item, amount);
    }

    public void deleteAll() {
        baseInventoryService.deleteAll();
        resourceInventoryService.deleteAll();
        playerInventoryService.deleteAll();
    }
}
