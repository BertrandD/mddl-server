package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.enums.ItemType;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.items.GameItem;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.repository.ItemRepository;
import com.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Service
public class ItemService {

    // Resources ids
    private static final String METAL = "100";
    private static final String CRYSTAL = "101";
    private static final String DEUTHERIUM = "102";

    @Autowired
    ItemRepository repository;

    @Autowired
    InventoryService inventoryService;

    public ItemInstance findOne(String id){
        return repository.findOne(id);
    }

    public List<ItemInstance> findAll(){
        return repository.findAll();
    }

    public ItemInstance create(String itemId, long count){
        GameItem tmpl = ItemData.getInstance().getTemplate(itemId);
        if(tmpl == null) return null;

        return repository.save(new ItemInstance(itemId, count));
    }

    public void update(ItemInstance item){
        repository.save(item);
    }

    public ItemInstance refresh(ItemInstance item)
    {
        if(item.getType().equals(ItemType.RESOURCE)) return refreshResource(item);

        final long diffTime = (System.currentTimeMillis() - item.getLastRefresh()) / 1000; // seconds
        if(diffTime > 0)
        {
            switch(item.getType())
            {
                case COMMON:
                case CARGO:
                case ENGINE:
                case MODULE:
                case STRUCTURE:
                case WEAPON:
            }
            update(item);
        }
        return item;
    }

    public ItemInstance refreshResource(ItemInstance item){
        final long diffTime = (System.currentTimeMillis() - item.getLastRefresh()) / 1000; // seconds
        if(diffTime > 0)
        {
            switch(item.getItemId())
            {
                case METAL:
                {
                    Utils.println("WORKS !");
                    item.setLastRefresh(System.currentTimeMillis());
                    /*final BuildingInstance metalmine = null; // TODO since 10 april 2016 at 19h26
                    if(metalmine != null) {
                        final long units = metalmine.getMineBuilding().getProductionByLevel().get(metalmine.getCurrentLevel());
                        item.setCount(item.getCount() + ((units / 3600) * diffTime));
                        item.setLastRefresh(System.currentTimeMillis());
                    }*/
                    break;
                }
                case CRYSTAL:
                {
                    break;
                }
                case DEUTHERIUM:
                {
                    break;
                }
            }
            update(item);
        }
        return item;
    }

    public void delete(String id){ repository.delete(id); }

    public void deleteAll(){ repository.deleteAll(); }
}
