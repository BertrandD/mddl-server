package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class FleetInventory extends Inventory{

    //@DBRef
    //private Fleet fleet;

    @DBRef
    @JsonView(View.Standard.class)
    private List<ItemInstance> items;

    public FleetInventory(){}

    public List<ItemInstance> getItems() {
        return items;
    }

    public void setItems(List<ItemInstance> items) {
        this.items = items;
    }


    @Override
    public boolean isAllowedToStore(ItemInstance item) {
        return false;
    }

    @Override
    public long getMaxCapacity() {
        return 0;
    }

    @Override
    public long getCurrentCapacityCharge() {
        return 0;
    }

    @Override
    public long getFreeCapacity() {
        return 0;
    }

    @Override
    public boolean addItem(ItemInstance item) {
        return false; // TODO
    }

    @Override
    public boolean removeItem(String id) {
        return false; // TODO
    }

    @Override
    public ItemInstance removeAndGet(String id) {
        return null; // TODO
    }
}
