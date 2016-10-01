package com.gameserver.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gameserver.model.vehicles.Fleet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author LEBOC Philippe
 */
public final class FleetInventory extends Inventory {

    @DBRef
    @JsonBackReference
    private Fleet fleet;

    public FleetInventory() {
        super();
    }

    public FleetInventory(Fleet fleet) {
        super();
        setId(new ObjectId().toString());
        setFleet(fleet);
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }
}
