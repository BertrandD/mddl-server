package com.gameserver.services.impl;

import com.gameserver.dao.FleetDao;
import com.gameserver.enums.VehicleMission;
import com.gameserver.model.commons.Coordinates;
import com.gameserver.model.vehicles.Fleet;
import com.gameserver.services.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class FleetServiceImpl implements FleetService {

    @Autowired
    private FleetDao fleetDao;

    @Override
    public Fleet create(Coordinates departure, Coordinates arrival, VehicleMission mission) {
        return fleetDao.insert(new Fleet(arrival, departure, mission));
    }

    @Override
    public Fleet findOne(String id) {
        return fleetDao.findOne(id);
    }

    @Override
    public List<Fleet> findAll() {
        return fleetDao.findAll();
    }

    @Override
    public void update(Fleet object) {
        fleetDao.save(object);
    }

    @Override
    public void remove(Fleet object) {
        fleetDao.delete(object);
    }

    @Override
    public void clearAll() {
        fleetDao.deleteAll();
    }
}
