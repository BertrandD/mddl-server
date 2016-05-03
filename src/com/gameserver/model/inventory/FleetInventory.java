package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.gameserver.model.instances.ItemInstance;
import com.gameserver.model.vehicles.Fleet;
import com.util.data.json.View;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashMap;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
public class FleetInventory extends Inventory{

    @DBRef
    @JsonManagedReference
    private Fleet fleet;

    public FleetInventory(){
        setItems(new HashMap<>());
    }

    public FleetInventory(Fleet fleet){
        setItems(new HashMap<>());
        setFleet(fleet);
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

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }
}
