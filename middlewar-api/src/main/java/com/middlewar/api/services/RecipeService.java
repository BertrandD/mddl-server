package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.items.SlotItem;
import com.middlewar.core.model.items.Structure;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class RecipeService {

    public RecipeInstance create(String name, Player owner, Structure structure, List<SlotItem> components) {
        final RecipeInstance recipeInstance = new RecipeInstance(-1, name, owner, structure, components);
        owner.getRecipes().add(recipeInstance);
        return recipeInstance;
    }
}
