package com.middlewar.core.model.vehicles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Weapon;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author LEBOC Philippe
 */
@Data
public abstract class Vehicle implements IShip {

    @Id
    private String id;

    private long count;

    @DBRef
    @JsonBackReference
    private Base base;

    private VehicleState state;

    @Override
    public double getDamage() {
        return getWeapons().stream().mapToDouble(Weapon::getDamage).sum() * getCount();
    }

    @Override
    public double getShield() {
        return 0;
    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public double getVolume() {
        return getStructure().getVolume() * getCount();
    }

    @Override
    public double getWeight() {
        return (getStructure().getWeight() +
                getEngines().stream().mapToDouble(Engine::getWeight).sum() +
                getModules().stream().mapToDouble(Module::getWeight).sum() +
                getWeapons().stream().mapToDouble(Weapon::getWeight).sum() +
                getCargos().stream().mapToDouble(Cargo::getWeight).sum()) * getCount();
    }

    @Override
    public double getMaxStorableVolume() {
        return getCargos().stream().mapToDouble(Cargo::getCapacity).sum() * getCount(); // TODO: change this shit
    }

    @Override
    public double getMaxStorableWeight() {
        return getCargos().stream().mapToDouble(Cargo::getCapacity).sum() * getCount(); // TODO: change this shit
    }
}
