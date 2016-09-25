package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.enums.Lang;
import com.gameserver.holders.BuildingHolder;
import com.gameserver.holders.ItemHolder;
import com.gameserver.model.Base;
import com.gameserver.model.commons.Requirement;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.items.Item;
import com.util.response.JsonResponse;
import com.util.response.JsonResponseType;
import com.util.response.SystemMessageId;
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
     * @param lang
     * @return null if everythings is OK, JsonResponse with proper error message otherwise
     */
    public JsonResponse validateBuildingRequirements(Base base, BuildingInstance building, HashMap<ItemInstance, Long> collector, Lang lang) {
        final Requirement requirements = building.getTemplate().getRequirements().get(building.getCurrentLevel()+1);
        if(requirements == null) return null;

        inventoryService.refresh(base);

        if(!validateBuildings(base, requirements)) {
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_BUILDING_REQUIREMENT);
        }

        if(!validateItems(base, requirements, collector)){
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_ITEM_REQUIREMENT);
        }

        return null;
    }

    public JsonResponse validateItemRequirements(Base base, Item item, HashMap<ItemInstance, Long> collector, Lang lang) {
        inventoryService.refresh(base);

        if(!validateBuildings(base, item.getRequirement())) {
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_BUILDING_REQUIREMENT);
        }

        if(!validateItems(base, item.getRequirement(), collector)){
            return new JsonResponse(JsonResponseType.ERROR, lang, SystemMessageId.YOU_DONT_MEET_ITEM_REQUIREMENT);
        }

        return null;
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
                final ResourceInventory rInventory = base.getResources().stream().filter(k -> k.getItem().getTemplateId().equalsIgnoreCase(holder.getId())).findFirst().orElse(null);
                if(rInventory == null || rInventory.getItem().getCount() < holder.getCount())
                    meetRequirements = false;
                else
                    collector.put(rInventory.getItem(), holder.getCount());
            }
            else
            {
                final ItemInstance iInst = bInventory.getItems().stream().filter(k -> k.getTemplateId().equalsIgnoreCase(holder.getId())).findFirst().orElse(null);
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
