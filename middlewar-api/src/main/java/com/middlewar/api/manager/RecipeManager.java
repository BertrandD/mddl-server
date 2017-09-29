package com.middlewar.api.manager;

import com.middlewar.api.exceptions.BadItemException;
import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.exceptions.NotEnoughSlotsException;
import com.middlewar.api.exceptions.RecipeCreationFailedException;
import com.middlewar.api.services.InventoryService;
import com.middlewar.api.services.RecipeService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.enums.Slot;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.items.GameItem;
import com.middlewar.core.model.items.Item;
import com.middlewar.core.model.items.SlotItem;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.stats.Stats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecipeManager {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private RecipeService recipeService;

    public RecipeInstance create(Player owner, String name, String structureID, List<String> componentsIds) throws ItemNotFoundException, RecipeCreationFailedException, NotEnoughSlotsException, BadItemException {
        Structure structure = ItemData.getInstance().getStructure(structureID);
        if (structure == null)
            throw new ItemNotFoundException();

        List<SlotItem> components = new LinkedList<>();

        for (String k : componentsIds) {
            GameItem component = ItemData.getInstance().getTemplate(k);
            if (component == null) {
                throw new ItemNotFoundException();
            }

            if (!(component instanceof SlotItem)) {
                throw new BadItemException();
            }

            if (structure.getAvailablesSlotForItem((SlotItem) component) <= 0) {
                throw new NotEnoughSlotsException();
            }

            components.add((SlotItem) component);
        }

        final RecipeInstance recipeInstance = recipeService.create(name, owner, structure, components);
        if (recipeInstance == null) throw new RecipeCreationFailedException();
        return recipeInstance;
    }
}
