package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.vehicles.Ship;
import com.gameserver.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class ShipService {

    @Autowired
    private ShipRepository repository;

    @Autowired
    private BaseService baseService;

    public Ship findOne(String id){
        return repository.findOne(id);
    }

    public Collection<Ship> findAll(){
        return repository.findAll();
    }

    public Ship create(Base base, String structure, long count){
        Ship ship = new Ship(base, structure, count);
        ship = repository.save(ship);
        base.getShips().add(ship);
        baseService.update(base);
        return ship;
    }

    public void update(Ship ship) { repository.save(ship); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }
}
