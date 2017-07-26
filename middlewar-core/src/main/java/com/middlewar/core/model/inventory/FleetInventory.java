package com.middlewar.core.model.inventory;

import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.vehicles.Fleet;
import lombok.Data;

import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Data
public final class FleetInventory extends Inventory {

    @OneToOne
    private Fleet fleet;

    public FleetInventory() {
        super();
    }

    public FleetInventory(Fleet fleet) {
        super();
        setFleet(fleet);
    }

    @Override
    public ItemInstance getItem(String id) {
        return getItemsToMap().getOrDefault(id, null);
    }

    @Override
    public long getAvailableCapacity() {
        // Sum of all Cargo of all Ships
        return getFleet().getShips().stream().mapToLong(ship -> ship.getCargos().stream().mapToLong(Cargo::getCapacity).sum()).sum();
    }
}
