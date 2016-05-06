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

    public BaseInventory create(Base base){
        return repository.save(new BaseInventory(base));
    }

    @Async
    public void update(BaseInventory inventory) { repository.save(inventory); }

    @Async
    public void delete(String id) { repository.delete(id); }

    public void deleteAll(){
        repository.deleteAll();
    }

}
