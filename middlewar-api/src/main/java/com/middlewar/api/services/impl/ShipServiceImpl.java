package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.ShipDao;
import com.middlewar.api.services.ShipService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ShipServiceImpl extends DefaultServiceImpl<Ship, ShipDao> implements ShipService {

    @Autowired
    private ShipDao shipDao;

    @Autowired
    private BaseDao baseDao;

    public Ship create(Base base, Long count, RecipeInstance recipeInstance) {

        final Ship ship = new Ship(base, recipeInstance, count);
        base.getShips().add(ship);

        shipDao.save(ship);
        baseDao.save(base);
        return ship;
    }
}
