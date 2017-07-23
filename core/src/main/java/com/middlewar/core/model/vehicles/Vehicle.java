package com.middlewar.core.model.vehicles;

import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Weapon;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author LEBOC Philippe
 */
@Data
@MappedSuperclass
public abstract class Vehicle implements IShip {

    @Id
    @GeneratedValue
    private long id;

    private long count;

    @ManyToOne
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
