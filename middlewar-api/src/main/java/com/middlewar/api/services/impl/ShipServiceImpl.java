package com.middlewar.api.services.impl;

import com.middlewar.api.dao.BaseDao;
import com.middlewar.api.dao.ShipDao;
import com.middlewar.api.services.ShipService;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.vehicles.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipDao shipDao;

    @Autowired
    private BaseDao baseDao;

    public Ship create(Base base, String structure, long count, List<String> ids) {

        final Ship ship = new Ship(base, structure, count);
        base.getShips().add(ship);

        if (ids != null && !ids.isEmpty()) {
            ship.setCargoIds(ids.stream().filter(k -> k.startsWith("cargo_")).collect(Collectors.toList()));
            ship.setEngineIds(ids.stream().filter(k -> k.startsWith("engine_")).collect(Collectors.toList()));
            ship.setModuleIds(ids.stream().filter(k -> k.startsWith("module_")).collect(Collectors.toList()));
            ship.setWeaponIds(ids.stream().filter(k -> k.startsWith("weapon_")).collect(Collectors.toList()));
        }

        shipDao.save(ship);
        baseDao.save(base);
        return ship;
    }

    @Override
    public Ship findOne(long id) {
        return shipDao.findOne(id);
    }

    @Override
    public List<Ship> findAll() {
        return shipDao.findAll();
    }

    @Override
    public void update(Ship object) {
        shipDao.save(object);
    }

    @Override
    public void remove(Ship object) {
        shipDao.delete(object);
    }

    @Override
    public void deleteAll() {
        shipDao.deleteAll();
    }
}
