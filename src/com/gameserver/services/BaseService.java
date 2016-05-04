package com.gameserver.services;

import com.gameserver.repository.BaseRepository;
import com.gameserver.model.Base;
import com.gameserver.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseService {

    @Autowired
    private BaseRepository repository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PlayerService playerService;

    public Base findOne(String id){
        return repository.findOne(id);
    }

    public Collection<Base> findAll(){
        return repository.findAll();
    }

    public Base create(String name, Player player){
        final Base base = new Base(name, player);
        base.setResources(inventoryService.createResourceInventory(base));
        base.setCommons(inventoryService.createBaseInventory(base));
        base.setShipItems(inventoryService.createBaseInventory(base));

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
