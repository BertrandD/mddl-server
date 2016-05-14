package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory implements IInventory {

    @Id
    @JsonView(View.Standard.class)
    private String id;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private HashMap<String, ItemInstance> items;

    public Inventory(){
        setItems(new HashMap<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, ItemInstance> getItems() {
        return items;
    }

    public void setItems(HashMap<String, ItemInstance> items) {
        this.items = items;
    }

    @Override
    public long getMaxWeight() {
        return 0;
    }

    @Override
    public long getWeight() {
        return 0;
    }

    @Override
    public long getFreeWeight() {
        return 0;
    }

    @Override
    public long getMaxVolume() {
        return 0;
    }

    @Override
    public long getVolume() {
        long volume = 0;
        for (ItemInstance item : items.values()) {
            volume += item.getTemplate().getVolume() * item.getCount();
        }
        return volume;
    }

    @Override
    public long getFreeVolume() {
        return getMaxVolume() - getVolume();
    }
}
