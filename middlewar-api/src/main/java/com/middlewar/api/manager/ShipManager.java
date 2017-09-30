package com.middlewar.api.manager;

import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.RecipeNotFoundException;
import com.middlewar.api.exceptions.ShipCreationFailedException;
import com.middlewar.api.services.InventoryService;
import com.middlewar.api.services.ShipService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShipManager {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ShipService shipService;

    public Ship create(Base base, Long count, long recipeId) {
        RecipeInstance recipe = base.getOwner().getRecipes().stream().filter(k->k.getId() == recipeId).findFirst().orElse(null);
        if (recipe == null) {
            throw new RecipeNotFoundException();
        }

        final BaseInventory inventory = base.getBaseInventory();
        final List<ItemInstance> collector = new ArrayList<>();

        final ItemInstance structuresInst = inventory.getItemsToMap().get(recipe.getStructure().getItemId());
        if (structuresInst == null || structuresInst.getCount() < count)
            throw new ItemRequirementMissingException();
        collector.add(structuresInst);

        for (GameItem template : recipe.getComponents()) {
            final ItemInstance inst = inventory.getItemsToMap().get(template.getItemId());
            if (inst != null && inst.getCount() >= count) collector.add(inst);
            else throw new ItemRequirementMissingException();
        }

        for (ItemInstance inst : collector)
            inventoryService.consumeItem(inst, count);

        final Ship ship = shipService.create(base, recipe, count);
        if (ship == null) throw new ShipCreationFailedException();
        return ship;
    }
}
