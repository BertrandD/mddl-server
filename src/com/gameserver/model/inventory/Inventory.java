package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gameserver.interfaces.IInventory;
import com.gameserver.model.instances.ItemInstance;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public abstract class Inventory implements IInventory {

    @Id
    private String id;

    @DBRef
    @JsonManagedReference
    private List<ItemInstance> items;

    public Inventory() {
        setId(new ObjectId().toString());
        setItems(new ArrayList<>());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }

    public long getHowManyCanBeAdded(long addAmount, long volume) {
        if(getFreeVolume() <= 0) return 0;
        if(addAmount * volume > getFreeVolume())
            return (long)Math.floor(getFreeVolume() / volume);
        return addAmount;
    }

    public double getHowManyCanBeAdded(double addAmount, long volume) {
        if(getFreeVolume() <= 0) return 0;
        if((addAmount * volume) > getFreeVolume())
            return getFreeVolume() / volume;
        return addAmount;
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
        for (ItemInstance item : items) {
            volume += item.getTemplate().getVolume() * item.getCount();
        }
        return volume;
    }

    @Override
    public long getFreeVolume() {
        return getMaxVolume() - getVolume();
    }
}
