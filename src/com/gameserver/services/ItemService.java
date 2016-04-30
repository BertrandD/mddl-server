package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.inventory.BaseInventory;
import com.gameserver.model.inventory.Inventory;
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

    public ItemInstance create(Base base, String itemId, long count){
        final GameItem tmpl = ItemData.getInstance().getTemplate(itemId);
        if(tmpl == null) return null;

        final BaseInventory inv;

        switch(tmpl.getType())
        {
            case RESOURCE:
                inv = base.getResources();
                break;
            case CARGO:
            case ENGINE:
            case MODULE:
            case WEAPON:
            case STRUCTURE:
                inv = base.getShipItems();
                break;
            default:
                inv = base.getCommons();
                break;
        }

        final ItemInstance inst = repository.save(new ItemInstance(inv, itemId, count));
        inv.addItem(inst);
        baseInventoryService.update(inv);

        return inst;
    }

    /**
     * put in player inventory
     * @param inventory of current player
     * @param itemId
     * @param count
     * @return
     */
    public ItemInstance create(Inventory inventory, String itemId, long count){
        final GameItem tmpl = ItemData.getInstance().getTemplate(itemId);
        if(tmpl == null) return null;

        final ItemInstance item = new ItemInstance(inventory, itemId, count);
        return repository.save(item);
    }

    public void update(ItemInstance item){
        repository.save(item);
    }

    public void delete(String id){ repository.delete(id); }

    public void deleteAll(){ repository.deleteAll(); }
}
