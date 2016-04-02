package com.gameserver.services;

import com.gameserver.data.xml.impl.ItemData;
import com.gameserver.model.Base;
import com.gameserver.model.items.Item;
import com.gameserver.model.items.ItemInstance;
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

    public ItemInstance findOne(String id){
        return repository.findOne(id);
    }

    public List<ItemInstance> findAll(){
        return repository.findAll();
    }

    public ItemInstance create(Base owner, String itemId, long count){
        Item tmpl = ItemData.getInstance().getTemplate(itemId);
        if(tmpl == null) return null;
        return repository.save(new ItemInstance(owner, itemId, count, tmpl));
    }

    public void update(ItemInstance item){
        repository.save(item);
    }

    public void delete(String id){ repository.delete(id); }

    public void deleteAll(){ repository.deleteAll(); }
}
