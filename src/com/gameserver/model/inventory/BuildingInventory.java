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
    public long getFreeCapacity() {
        return getMaxCapacity() - getCurrentCapacityCharge();
    }

    public BuildingInstance getBuilding() {
        return building;
    }

    public void setBuilding(BuildingInstance building) {
        this.building = building;
    }
}
