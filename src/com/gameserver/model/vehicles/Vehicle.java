package com.gameserver.model.vehicles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gameserver.enums.VehicleState;
import com.gameserver.model.Base;
import com.gameserver.model.items.Cargo;
import com.gameserver.model.items.Engine;
import com.gameserver.model.items.Module;
import com.gameserver.model.items.Weapon;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * @author LEBOC Philippe
 */
public abstract class Vehicle implements IShip {

    @Id
    private String id;
    private long count;

    @DBRef
    @JsonBackReference
    private Base base;
    private VehicleState state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public VehicleState getState() {
        return state;
    }

    public void setState(VehicleState state) {
        this.state = state;
    }

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
        return getCargos().stream().mapToDouble(Cargo::getCapacity).sum() * getCount(); // TODO: change capacity for capacity volume
    }

    @Override
    public double getMaxStorableWeight() {
        return getCargos().stream().mapToDouble(Cargo::getCapacity).sum() * getCount(); // TODO: change capacity for capacity weight
    }
}
