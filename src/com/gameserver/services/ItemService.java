package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
import com.gameserver.model.inventory.PlayerInventory;
import com.gameserver.model.items.GameItem;
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

    @Autowired
    private PlayerInventoryService playerInventoryService;

    @Autowired
    private BaseInventoryService baseInventoryService;

    public ItemInstance findOne(String id){
        return repository.findOne(id);
    }

    public List<ItemInstance> findAll(){
        return repository.findAll();
    }

    public ItemInstance create(Inventory inv, String itemId, long count){

        final ItemInstance inst = repository.save(new ItemInstance(inv, itemId, count)); // TODO: Its bad. Need to check inventory capacity before !
        inv.addItem(inst);

        if(inv instanceof BaseInventory)
            baseInventoryService.update((BaseInventory)inv);
        else if(inv instanceof PlayerInventory)
            playerInventoryService.update((PlayerInventory)inv);
        else return null;

        return inst;
    }

    public void update(ItemInstance item){
        repository.save(item);
    }

    public void delete(String id){ repository.delete(id); }

    public void deleteAll(){ repository.deleteAll(); }
}
