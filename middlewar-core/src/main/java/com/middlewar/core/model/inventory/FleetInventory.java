package com.middlewar.core.model.inventory;

import com.middlewar.core.model.instances.ItemInstance;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.vehicles.Fleet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToOne;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@NoArgsConstructor
public final class FleetInventory extends Inventory {

    @OneToOne
    private Fleet fleet;

    public FleetInventory(Fleet fleet) {
        super();
        setFleet(fleet);
    }

    @Override
    public ItemInstance getItem(String id) {
        return getItems()
                .stream()
                .filter(i -> i.getTemplateId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public long getAvailableCapacity() {
        // Sum of all Cargo of all Ships
        return getFleet().getShips().stream()
                .mapToLong(ship -> ship.getCargos().stream().mapToLong(Cargo::getCapacity).sum())
                .sum();
    }
}
