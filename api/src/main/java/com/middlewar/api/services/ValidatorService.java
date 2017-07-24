package com.middlewar.api.services;

import com.middlewar.api.exceptions.BuildingRequirementMissingException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.ItemType;
import com.middlewar.core.holders.BuildingHolder;
import com.middlewar.core.holders.ItemHolder;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.commons.Requirement;
import com.middlewar.core.model.instances.BuildingInstance;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.inventory.Resource;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
@Service
public class ValidatorService {

    @Autowired
    private InventoryService inventoryService;

    /**
     * Used to validate a building construction / upgrade
     * @param base the current base
     * @param building the current building to be building / upgraded
     * @param collector the collector that collecting items to be consumed
     * @return null if everythings is OK, Response with proper error message otherwise
     */
    public void validateBuildingRequirements(Base base, BuildingInstance building, HashMap<ItemInstance, Long> collector) throws BuildingRequirementMissingException, ItemRequirementMissingException {
        final Requirement requirements = building.getTemplate().getRequirements().get(building.getCurrentLevel()+1);
        if(requirements == null) return;

        inventoryService.refresh(base);

        if(!validateBuildings(base, requirements)) {
            throw new BuildingRequirementMissingException();
        }

        if(!validateItems(base, requirements, collector)){
            throw new ItemRequirementMissingException();
        }
    }

    public void validateItemRequirements(Base base, Item item, HashMap<ItemInstance, Long> collector) throws BuildingRequirementMissingException, ItemRequirementMissingException {
        inventoryService.refresh(base);

        if(!validateBuildings(base, item.getRequirement())) {
            throw new BuildingRequirementMissingException();
        }

        if(!validateItems(base, item.getRequirement(), collector)){
            throw new ItemRequirementMissingException();
        }
    }

    public boolean validateBuildings(Base base, Requirement requirements) {
        int i = 0;
        boolean meetRequirements = true;
        while(meetRequirements && i < requirements.getBuildings().size())
        {
            final BuildingHolder holder = requirements.getBuildings().get(i);
            final BuildingInstance bInst = base.getBuildings().stream().filter(k -> k.getBuildingId().equals(holder.getTemplateId())).findFirst().orElse(null);
            if(bInst == null || bInst.getCurrentLevel() < holder.getLevel()) {
                meetRequirements = false;
            }
            i++;
        }

        return meetRequirements;
    }

    public boolean validateItems(Base base, Requirement requirements, HashMap<ItemInstance, Long> collector) {
        int i = 0;
        boolean meetRequirements = true;
        while(meetRequirements && i < requirements.getItems().size())
        {
            final ItemHolder holder = requirements.getItems().get(i);
            final BaseInventory bInventory = base.getBaseInventory();

            final GameItem template = ItemData.getInstance().getTemplate(holder.getId());

            if(template.getType().equals(ItemType.RESOURCE))
            {
                final Resource rInventory = base.getResources().stream().filter(k -> k.getItem().getTemplateId().equalsIgnoreCase(holder.getId())).findFirst().orElse(null);
                if(rInventory == null || rInventory.getCount() < holder.getCount())
                    meetRequirements = false;
                else
                    collector.put(rInventory.getItem(), holder.getCount());
            }
            else
            {
                final ItemInstance iInst = bInventory.getItemsToMap().get(holder.getId());
                if(iInst == null || iInst.getCount() < holder.getCount())
                    meetRequirements = false;
                else
                    collector.put(iInst, holder.getCount());
            }
            i++;
        }
        return meetRequirements;
    }
}
