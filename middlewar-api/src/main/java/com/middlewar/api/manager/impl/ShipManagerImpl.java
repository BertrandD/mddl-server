package com.middlewar.api.manager.impl;

import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.ShipCreationFailedException;
import com.middlewar.api.manager.ShipManager;
import com.middlewar.api.services.ShipService;
import com.middlewar.api.services.InventoryService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipManagerImpl implements ShipManager {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ShipService shipService;

    public Ship create(Base base, Long count, String structure, List<String> ids) {
        if (ItemData.getInstance().getStructure(structure) == null) throw new ItemNotFoundException();

        final BaseInventory inventory = base.getBaseInventory();
        final List<ItemInstance> collector = new ArrayList<>();

        final ItemInstance structuresInst = inventory.getItemsToMap().get(structure);
        if (structuresInst == null || structuresInst.getCount() < count)
            throw new ItemRequirementMissingException();

        for (String id : ids) {
            final GameItem template = ItemData.getInstance().getTemplate(id);
            if (template == null) throw new ItemNotFoundException();
            else {
                final ItemInstance inst = inventory.getItemsToMap().get(template.getItemId());
                if (inst != null && inst.getCount() >= count) collector.add(inst);
                else throw new ItemRequirementMissingException();
            }
        }

        for (ItemInstance inst : collector)
            inventoryService.consumeItem(inst, 1);

        final Ship ship = null; //shipService.create(base, structure, count, ids);
        if (ship == null) throw new ShipCreationFailedException();
        return ship;
    }
}
