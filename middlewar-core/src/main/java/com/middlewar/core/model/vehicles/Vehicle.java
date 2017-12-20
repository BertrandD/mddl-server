package com.middlewar.core.model.vehicles;

import com.middlewar.core.enums.ItemType;
import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@MappedSuperclass
public abstract class Vehicle implements IShip {

    @Id
    @GeneratedValue
    private long id;

    private long count;

    @ManyToOne
    private Base base;

    private VehicleState state;

    private RecipeInstance recipeInstance;

    public Vehicle(Base base, RecipeInstance recipeInstance, long count) {
        this.base = base;
        this.recipeInstance = recipeInstance;
        this.count = count;
    }

    @Override
    public Structure getStructure() {
        return recipeInstance.getStructure();
    }

    @Override
    public List<Cargo> getCargos() {
        return recipeInstance.getComponents().stream().filter(k->k.getType().equals(ItemType.CARGO)).map(k->(Cargo)k).collect(Collectors.toList());
    }

    @Override
    public List<Engine> getEngines() {
        return recipeInstance.getComponents().stream().filter(k->k.getType().equals(ItemType.ENGINE)).map(k->(Engine)k).collect(Collectors.toList());
    }

    @Override
    public List<Module> getModules() {
        return recipeInstance.getComponents().stream().filter(k->k.getType().equals(ItemType.MODULE)).map(k->(Module)k).collect(Collectors.toList());
    }

    @Override
    public List<Weapon> getWeapons() {
        return recipeInstance.getComponents().stream().filter(k->k.getType().equals(ItemType.WEAPON)).map(k->(Weapon)k).collect(Collectors.toList());
    }

    @Override
    public double getDamage() {
        return recipeInstance.calcDamage();
    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public double getVolume() {
        throw new UnsupportedOperationException();
    }
}
