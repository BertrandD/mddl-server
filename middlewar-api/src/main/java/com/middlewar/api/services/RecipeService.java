package com.middlewar.api.services;

import com.middlewar.api.dao.RecipeDao;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.Recipe;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface RecipeService extends DefaultService<Recipe, RecipeDao> {
    Recipe create(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> weapons);
}
