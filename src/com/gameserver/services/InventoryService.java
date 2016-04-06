package com.gameserver.services;

import com.gameserver.model.Player;
import com.gameserver.model.buildings.Building;
import com.gameserver.model.buildings.Storage;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.inventory.StorageBuildingInventory;
import com.gameserver.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author LEBOC Philippe
 */
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private BuildingService buildingService;

    public Inventory findOne(String id){
        return repository.findOne(id);
    }

    public Collection<Inventory> findAll(){
        return repository.findAll();
    }

    public Inventory create(Player player){
        PlayerInventory inventory = new PlayerInventory(player);
        inventory = repository.save(inventory);
        player.setInventory(inventory);
        playerService.update(player);
        return inventory;
    }

    public Inventory create(BuildingInstance building){
        StorageBuildingInventory inventory = new StorageBuildingInventory(building);
        inventory = repository.save(inventory);
        building.setInventory(inventory);
        buildingService.update(building);
        return inventory;
    }

    public void update(Inventory inventory) { repository.save(inventory); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }

}
