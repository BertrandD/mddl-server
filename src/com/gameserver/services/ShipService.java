package com.gameserver.services;

import com.gameserver.model.vehicles.Ship;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ShipService extends DatabaseService<Ship>{

    protected ShipService() {
        super(Ship.class);
    }

    @Override
    public Ship create(Object... params) {
        if(params.length != 1) return null;
        final Ship ship = (Ship) params[0];
        mongoOperations.insert(ship);
        return ship;
    }
}
