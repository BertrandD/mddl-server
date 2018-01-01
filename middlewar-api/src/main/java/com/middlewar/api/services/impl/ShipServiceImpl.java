package com.middlewar.api.services.impl;

import com.middlewar.api.services.BaseService;
import com.middlewar.api.services.ShipService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;
import com.middlewar.core.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author LEBOC Philippe
 */
@Service
@Validated
public class ShipServiceImpl extends CrudServiceImpl<Ship, Integer, ShipRepository> implements ShipService {

    @Autowired
    private BaseService baseService;

    @Override
    public Ship create(@NotNull Base base, @NotNull RecipeInstance recipeInstance, @Min(1) long count) {

        final Ship ship = repository.save(new Ship(base, recipeInstance, count));

        // TODO UPDATE BASE
        if(ship != null) {
            base.getShips().add(ship);
            baseService.update(base);
        }

        return ship;
    }
}
