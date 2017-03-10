package com.middlewar.core.model.inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.vehicles.Fleet;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author LEBOC Philippe
 */
@Data
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

    @Override
    public ItemInstance getItem(String id) {
        return getItemsToMap().containsKey(id) ? getItemsToMap().get(id) : null;
    }

    @Override
    public long getAvailableCapacity() {
        // Sum of all Cargo of all Ships
        return getFleet().getShips().stream().mapToLong(ship -> ship.getCargos().stream().mapToLong(Cargo::getCapacity).sum()).sum();
    }
}
