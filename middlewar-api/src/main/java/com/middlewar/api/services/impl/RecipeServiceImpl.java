package com.middlewar.api.services.impl;

import com.middlewar.api.services.RecipeService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.items.SlotItem;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.repository.RecipeRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
@Validated
public class RecipeServiceImpl extends CrudServiceImpl<RecipeInstance, Integer, RecipeRepository> implements RecipeService {

    @Override
    public RecipeInstance create(@NotEmpty String name, @NotNull Player owner, @NotNull Structure structure, @NotEmpty List<SlotItem> components) {
        final RecipeInstance recipeInstance = repository.save(new RecipeInstance(-1, name, owner, structure, components));

        if(recipeInstance == null)
            throw new RuntimeException(); // TODO: create exception

        owner.getRecipes().add(recipeInstance); // TODO: move me

        return recipeInstance;
    }
}
