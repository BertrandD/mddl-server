package com.gameserver.services;

import com.gameserver.enums.VehicleMission;
import com.gameserver.model.commons.Coordinates;
import com.gameserver.model.vehicles.Fleet;
import com.gameserver.repository.FleetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class FleetService {

    @Autowired
    private FleetRepository repository;

    @Autowired
    private ShipService shipService;

    public Fleet findOne(String id){
        return repository.findOne(id);
    }

    public Collection<Fleet> findAll(){
        return repository.findAll();
    }

    public Fleet create(Coordinates from, Coordinates to, VehicleMission mission){
        Fleet fleet = new Fleet(from, to, mission);
        fleet = repository.save(fleet);
        // TODO: Manage with Coordinates
        return fleet;
    }

    public void update(Fleet ship) { repository.save(ship); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }

}
