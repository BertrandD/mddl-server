package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashMap;

/**
 * @author LEBOC Philippe
 */
public class BuildingInventory extends Inventory {

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private HashMap<String, ItemInstance> items;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private BuildingInstance building;

    public BuildingInventory(){
        setItems(new HashMap<>());
    }

    public BuildingInventory(BuildingInstance building){
        setItems(new HashMap<>());
        setBuilding(building);
    }

    @Override
    public boolean isAllowedToStore(ItemInstance item) {
        return building.getStorageBuilding().getFilter().getIds().contains(item.getItemId());
    }

    @Override
    public long getMaxCapacity() {
        return building.getStorageBuilding().getCapacityByLevel(building.getCurrentLevel());
    }

    @Override
    public long getCurrentCapacityCharge() {
        long weight = 0;
        for(ItemInstance item : items.values())
        {
            weight += item.getWeight();
        }
        return weight;
    }

    @Override
    public long getFreeCapacity() {
        return getMaxCapacity() - getCurrentCapacityCharge();
    }

    @Override
    public boolean addItem(ItemInstance item) {
        if(isAllowedToStore(item)) {
            if (getItems().isEmpty() && getFreeCapacity() >= item.getWeight()) {
                getItems().put(item.getItemId(), item);
                // TODO: addItem(ItemInstance item, boolean force) => if capacity < item.weight (because item.count very high) add and destoy the rest
                return true;
            }

            // Override count if exist
            final ItemInstance it = items.get(item.getItemId());
            if (it != null) {
                if (getFreeCapacity() > getCurrentCapacityCharge() + item.getWeight()) {
                    it.setCount(it.getCount() + item.getCount());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeItem(String id) {
        return false; // TODO
    }

    @Override
    public ItemInstance removeAndGet(String id) {
        return null; // TODO
    }

    public HashMap<String, ItemInstance> getItems() {
        return items;
    }

    public void setItems(HashMap<String, ItemInstance> items) {
        this.items = items;
    }

    public BuildingInstance getBuilding() {
        return building;
    }

    public void setBuilding(BuildingInstance building) {
        this.building = building;
    }
}
