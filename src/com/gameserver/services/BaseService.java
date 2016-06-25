package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.Player;
import com.gameserver.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseService {

    @Autowired
    private MongoOperations operations;

    @Autowired
    private BaseRepository repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PlayerService playerService;

    public Base findOne(String id) {
        final Base base = repository.findOne(id);
        if(base != null) inventoryService.refreshResource(base);
        return base;
    }

    public Collection<Base> findAll() {
        final Collection<Base> bases = repository.findAll();
        for (Base base : bases) {
            inventoryService.refreshResource(base);
        }
        return bases;
    }

    public Base create(String name, Player player){
        final Base base = new Base(name, player);
        base.setBaseInventory(inventoryService.createBaseInventory(base));

        player.addBase(base);
        player.setCurrentBase(base);
        playerService.update(player);
        return repository.save(base);
    }

    public void update(Base b) { repository.save(b); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }
}
