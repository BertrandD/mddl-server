package com.middlewar.api.services;

import com.middlewar.api.dao.ShipDao;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.Recipe;
import com.middlewar.core.model.vehicles.Ship;

/**
 * @author Leboc Philippe.
 */
public interface ShipService extends DefaultService<Ship, ShipDao> {
    Ship create(Base base, Long count, Recipe recipe);
}
