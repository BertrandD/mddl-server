package com.middlewar.api.manager;

import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.ItemRequirementMissingException;
import com.middlewar.api.exceptions.RecipeNotFoundException;
import com.middlewar.api.exceptions.RecipeNotOwnedException;
import com.middlewar.api.exceptions.ShipCreationFailedException;
import com.middlewar.api.services.RecipeService;
import com.middlewar.api.services.ShipService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.instances.Recipe;
import com.middlewar.core.model.inventory.BaseInventory;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ShipManager {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ShipService shipService;

    @Autowired
    private RecipeService recipeService;

    public Ship create(Base base, Long count, long recipeId) throws ItemNotFoundException, ItemRequirementMissingException, ShipCreationFailedException, RecipeNotFoundException, RecipeNotOwnedException {

        Recipe recipe = recipeService.findOne(recipeId);
        if (recipe == null) {
            throw new RecipeNotFoundException();
        }

        if (!recipe.getOwner().equals(base.getOwner())) {
            throw new RecipeNotOwnedException();
        }

        final BaseInventory inventory = base.getBaseInventory();
        final List<ItemInstance> collector = new LinkedList<>();

        final ItemInstance structuresInst = inventory.getItemsToMap().get(recipe.getStructureId());
        if (structuresInst == null || structuresInst.getCount() < count)
            throw new ItemRequirementMissingException();

        for (String template: recipe.getAttachmentsIds()) {
            final ItemInstance inst = inventory.getItemsToMap().get(template);
            if (inst != null && inst.getCount() >= count) collector.add(inst);
            else throw new ItemRequirementMissingException();
        }

        for (ItemInstance inst : collector)
            inventoryService.consumeItem(inst, 1);

        final Ship ship = shipService.create(base, count, recipe);
        if (ship == null) throw new ShipCreationFailedException();
        return ship;
    }
}
