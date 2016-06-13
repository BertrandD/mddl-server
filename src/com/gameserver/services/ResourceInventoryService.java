package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.inventory.ResourceInventory;
import com.gameserver.repository.ResourceInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class ResourceInventoryService {

    @Autowired
    private ResourceInventoryRepository repository;

    @Autowired
    private ItemService itemService;

    public ResourceInventory findOne(String id){
        return repository.findOne(id);
    }

    public ResourceInventory findByBase(String id) { return repository.findByBase(id); }

    public ResourceInventory create(Base base){
        ResourceInventory inventory = new ResourceInventory(base);
        inventory = repository.save(inventory);
        return inventory;
    }

    @Async
    public void update(ResourceInventory inventory) { repository.save(inventory); }

    @Async
    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }

}
