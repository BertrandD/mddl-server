package com.middlewar.api.manager;

import com.middlewar.api.exceptions.ItemNotFoundException;
import com.middlewar.api.services.RecipeService;
import com.middlewar.api.services.ShipService;
import com.middlewar.api.services.impl.InventoryService;
import com.middlewar.core.data.xml.ItemData;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeManager {

    @Autowired
    private RecipeService recipeService;

    public RecipeInstance create(Player player, String name, String structure, List<String> cargos, List<String> engines, List<String> modules, List<String> weapons) throws ItemNotFoundException{

        if (ItemData.getInstance().getStructure(structure) == null)
            throw new ItemNotFoundException();

        for (String k : cargos) {
            if (ItemData.getInstance().getCargo(k) == null) {
                throw new ItemNotFoundException();
            }
        }
        for (String k : engines) {
            if (ItemData.getInstance().getEngine(k) == null) {
                throw new ItemNotFoundException();
            }
        }
        for (String k : modules) {
            if (ItemData.getInstance().getModule(k) == null) {
                throw new ItemNotFoundException();
            }
        }
        for (String k : weapons) {
            if (ItemData.getInstance().getWeapon(k) == null) {
                throw new ItemNotFoundException();
            }
        }

        return recipeService.create(name, player, structure, cargos, engines, modules, weapons);
    }

    private void checkItemExists(String item) {

    }
}
