package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.RecipeDao;
import com.middlewar.api.dao.ShipDao;
import com.middlewar.api.services.RecipeService;
import com.middlewar.api.services.ShipService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.Player;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Service
public class RecipeServiceImpl extends DefaultServiceImpl<RecipeInstance, RecipeDao> implements RecipeService {

    @Autowired
    private RecipeDao recipeDao;

    @Override
    public RecipeInstance create(String name, Player owner, String structureId, List<String> cargos, List<String> engines, List<String> modules, List<String> weapons) {
        RecipeInstance recipeInstance = new RecipeInstance(name, owner, structureId, cargos, engines, modules, weapons);

        owner.getRecipes().add(recipeInstance);

        recipeDao.save(recipeInstance);

        return recipeInstance;
    }
}
