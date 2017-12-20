package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ShipService implements DefaultService<Ship> {

    private int nextId = 0;

    public Ship create(Base base, RecipeInstance recipeInstance, long count) {

        final Ship ship = new Ship(base, recipeInstance, count);
        base.getShips().add(ship);

        return ship;
    }

    @Override
    public void delete(Ship o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Ship findOne(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextId() {
        return ++nextId;
    }
}
