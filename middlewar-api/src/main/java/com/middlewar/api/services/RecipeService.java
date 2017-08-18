package com.middlewar.api.services;

import com.middlewar.api.dao.RecipeDao;
import com.middlewar.api.dao.ShipDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;

import java.util.List;

/**
 * @author Leboc Philippe.
 */
public interface RecipeService extends DefaultService<RecipeInstance, RecipeDao> {
    RecipeInstance create(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> weapons);
}
