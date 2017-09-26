package com.middlewar.api.services;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Service
public class ShipService implements DefaultService<Ship> {

    private int nextId = 0;

    public Ship create(Base base, String structure, long count, List<String> ids) {

        final Ship ship = new Ship(base, structure, count);
        base.getShips().add(ship);

        if (ids != null && !ids.isEmpty()) {
            ship.setCargoIds(ids.stream().filter(k -> k.startsWith("cargo_")).collect(Collectors.toList()));
            ship.setEngineIds(ids.stream().filter(k -> k.startsWith("engine_")).collect(Collectors.toList()));
            ship.setModuleIds(ids.stream().filter(k -> k.startsWith("module_")).collect(Collectors.toList()));
            ship.setWeaponIds(ids.stream().filter(k -> k.startsWith("weapon_")).collect(Collectors.toList()));
        }

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
