package com.gameserver.services;

import com.gameserver.model.Base;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.repository.BaseInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author LEBOC Philippe
 */
@Service
public class BaseInventoryService {

    @Autowired
    private BaseInventoryRepository repository;

    public BaseInventory findOne(String id){
        return repository.findOne(id);
    }

    public BaseInventory findByBase(String id) { return repository.findByBase(id); }

    public BaseInventory create(Base base) {
        final BaseInventory inventory = new BaseInventory(base);
        inventory.setLastRefresh(System.currentTimeMillis());
        return repository.save(inventory);
    }

    @Async
    public synchronized void updateAsync(BaseInventory inventory) { repository.save(inventory); }

    public synchronized void update(BaseInventory inventory) { repository.save(inventory); }

    @Async
    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }

}
