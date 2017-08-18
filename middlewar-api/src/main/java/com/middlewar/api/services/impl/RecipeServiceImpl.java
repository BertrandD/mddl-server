package com.middlewar.api.services.impl;

import com.middlewar.api.dao.RecipeDao;
import com.middlewar.api.services.RecipeService;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class RecipeServiceImpl extends DefaultServiceImpl<Recipe, RecipeDao> implements RecipeService {

    @Autowired
    private RecipeDao recipeDao;

    @Override
    public Recipe create(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> weapons) {
        Recipe recipe = new Recipe(name, owner, structureId, cargos, engines, modules, weapons);

        owner.getRecipes().add(recipe);

        recipeDao.save(recipe);

        return recipe;
    }
}
