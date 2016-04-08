package com.gameserver.services;

import com.gameserver.data.xml.impl.BuildingData;
import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.buildings.Mine;
import com.gameserver.model.instances.BuildingInstance;
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
    ItemRepository repository;

    @Autowired
    BuildingService buildingService;

    public ItemInstance findOne(String id){
        return repository.findOne(id);
    }

    public List<ItemInstance> findAll(){
        return repository.findAll();
    }

    public ItemInstance create(String itemId, long count){
        GameItem tmpl = ItemData.getInstance().getTemplate(itemId);
        if(tmpl == null) return null;
        return repository.save(new ItemInstance(itemId, count, tmpl));
    }

    public void update(ItemInstance item){
        repository.save(item);
    }

    public ItemInstance refresh(ItemInstance item)
    {
        long diffTime = (System.currentTimeMillis() - item.getLastRefresh()) / 1000; // seconds
        if(diffTime > 0)
        {
            // TODO: Find a fix
            Mine metalMine = (Mine)BuildingData.getInstance().getBuilding("10004");
            long units = metalMine.getProductionByLevel().get(5);

            item.setCount(item.getCount() + ((units/3600) * diffTime));
            item.setLastRefresh(System.currentTimeMillis());
            update(item);
        }
        return item;
    }

    public void delete(String id){ repository.delete(id); }

    public void deleteAll(){ repository.deleteAll(); }
}
