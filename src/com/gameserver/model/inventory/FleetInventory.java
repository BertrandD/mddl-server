package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gameserver.model.vehicles.Fleet;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author LEBOC Philippe
 */
public final class FleetInventory extends AbstractMultiStorageInventory {

    @DBRef
    @JsonBackReference
    private Fleet fleet;

    public FleetInventory() {
        super();
    }

    public FleetInventory(Fleet fleet) {
        super();
        setFleet(fleet);
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
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
        return 0;
    }

    @Override
    public long getFreeVolume() {
        return 0;
    }
}
