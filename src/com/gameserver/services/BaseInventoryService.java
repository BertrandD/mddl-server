package com.gameserver.services;

import com.config.Config;
import com.gameserver.model.Base;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.repository.BaseInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseInventoryService {

    @Autowired
    private BaseInventoryRepository repository;

    @Autowired
    private ItemService itemService;

    public BaseInventory findOne(String id){
        return repository.findOne(id);
    }

    public BaseInventory findByBase(String id) { return repository.findByBase(id); }

    public BaseInventory create(Base base){
        BaseInventory inventory = new BaseInventory(base);
        inventory = repository.save(inventory);
        inventory.getItems().add(itemService.create(inventory, "metal", Config.INITIAL_BASE_METAL));
        return inventory;
    }

    public void update(BaseInventory inventory) { repository.save(inventory); }

    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }

}
