package com.middlewar.core.model.vehicles;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.Recipe;
import com.middlewar.core.model.items.Cargo;
import com.middlewar.core.model.items.Engine;
import com.middlewar.core.model.items.Module;
import com.middlewar.core.model.items.Structure;
import com.middlewar.core.model.items.Weapon;
import com.middlewar.core.serializer.ShipSerializer;
import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = ShipSerializer.class)
public class Ship extends Vehicle {

    public Ship() {
        super();
    }

    public Ship(Base base, Recipe recipe, long count) {
        super();
        setBase(base);
        this.setRecipe(recipe);
        setCount(count);
        setState(VehicleState.BASED);
    }

    @Override
    public Structure getStructure() {
        return this.getRecipe().getStructure();
    }

    @Override
    public List<Cargo> getCargos() {
        return this.getRecipe().getCargos();
    }

    @Override
    public List<Engine> getEngines() {
        return this.getRecipe().getEngines();
    }

    @Override
    public List<Module> getModules() {
        return this.getRecipe().getModules();
    }

    @Override
    public List<Weapon> getWeapons() {
        return this.getRecipe().getWeapons();
    }
}
