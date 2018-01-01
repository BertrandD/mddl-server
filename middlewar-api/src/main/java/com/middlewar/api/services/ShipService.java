package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Leboc Philippe.
 */
public interface ShipService extends CrudService<Ship, Integer> {
    Ship create(@NotNull Base base, @NotNull RecipeInstance recipeInstance, @Min(1) long count);
}
