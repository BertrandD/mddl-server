package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.vehicles.Ship;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        if(params.length < 3) return null;
        final Base base = (Base) params[0];
        final String structure = (String) params[1];
        final long count = (long) params[2];
        List<String> ids = null;

        if(params.length > 3)
            ids = (List<String>) params[3];

        final Ship ship = new Ship(base, structure, count);
        if(ids != null) {
            ship.setCargoIds(ids.stream().filter(k -> k.startsWith("cargo_")).collect(Collectors.toList()));
            ship.setEngineIds(ids.stream().filter(k -> k.startsWith("engine_")).collect(Collectors.toList()));
            ship.setModuleIds(ids.stream().filter(k -> k.startsWith("module_")).collect(Collectors.toList()));
            ship.setWeaponIds(ids.stream().filter(k -> k.startsWith("weapon_")).collect(Collectors.toList()));
        }
        mongoOperations.insert(ship);
        return ship;
    }
}
