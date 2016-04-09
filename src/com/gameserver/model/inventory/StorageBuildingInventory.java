package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.BuildingInstance;
import com.gameserver.model.instances.ItemInstance;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author LEBOC Philippe
 */
public class StorageBuildingInventory extends Inventory {

    @DBRef
    @JsonView(View.Standard.class)
    private ItemInstance item;

    @DBRef
    @JsonManagedReference
    @JsonView(View.Standard.class)
    private BuildingInstance building;

    public StorageBuildingInventory(){}

    public StorageBuildingInventory(BuildingInstance building){
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
        if(item != null) return item.getWeight();
        return 0;
    }

    @Override
    public long getFreeCapacity() {
        return getMaxCapacity() - getCurrentCapacityCharge();
    }

    @Override
    public boolean addItem(ItemInstance item) {

        if(getItem() == null && isAllowedToStore(item)){
            setItem(item); // TODO Check capacity before add
            return true;
        }

        if(item.getItemId().equals(getItem().getItemId())){
            if(getFreeCapacity() > getCurrentCapacityCharge() + (item.getWeight())){
                getItem().setCount(getItem().getCount() + item.getCount());
                // TODO save ItemInstance with new "count" to database
                // TODO delete added item from database
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

    public ItemInstance getItem() {
        return item;
    }

    public void setItem(ItemInstance item) {
        this.item = item;
    }

    public BuildingInstance getBuilding() {
        return building;
    }

    public void setBuilding(BuildingInstance building) {
        this.building = building;
    }
}
