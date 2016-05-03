package com.gameserver.services;

import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    public ItemInstance findOne(String id){
        return repository.findOne(id);
    }

    public ItemInstance findFirstByInventoryAndTemplateId(Inventory inventory, String templateId){
        return repository.findFirstByInventoryAndTemplateId(inventory, templateId);
    }

    public List<ItemInstance> findAll(){
        return repository.findAll();
    }

    /**
     * MANUALLY USE IS FORBIDDEN !
     * This method is managed by Inventory to made all necessary checks.
     * @param inventory where item need to be putted
     * @param itemId of the new item
     * @param count is the number of items created (stack)
     * @return the new created item
     */
    public ItemInstance create(Inventory inventory, String itemId, long count){
        return repository.save(new ItemInstance(inventory, itemId, count));
    }

    public void update(ItemInstance item){
        repository.save(item);
    }

    public void delete(String id){ repository.delete(id); }

    public void deleteAll(){ repository.deleteAll(); }
}
