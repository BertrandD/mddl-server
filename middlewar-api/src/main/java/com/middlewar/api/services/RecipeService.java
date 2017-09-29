package com.middlewar.api.services;

import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.items.Item;
import com.middlewar.core.model.items.SlotItem;
import com.middlewar.core.model.items.Structure;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class RecipeService implements DefaultService<RecipeInstance> {

    private int nextId = 0;

    public RecipeInstance create(String name, Player owner, Structure structure, List<SlotItem> components) {
        final RecipeInstance recipeInstance = new RecipeInstance(nextId(), name, owner, structure, components);

        owner.getRecipes().add(recipeInstance);

        return recipeInstance;
    }

    @Override
    public void delete(RecipeInstance o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecipeInstance findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextId() {
        return ++nextId;
    }
}
