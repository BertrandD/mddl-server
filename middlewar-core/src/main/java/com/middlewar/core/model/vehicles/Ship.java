package com.middlewar.core.model.vehicles;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.enums.VehicleState;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
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

    public Ship(Base base, RecipeInstance recipeInstance, long count) {
        super();
        setBase(base);
        setRecipeInstance(recipeInstance);
        setCount(count);
        setState(VehicleState.BASED);
    }

    @Override
    public Structure getStructure() {
        return getRecipeInstance().getStructure();
    }

    @Override
    public List<Cargo> getCargos() {
        return getRecipeInstance().getCargos();
    }

    @Override
    public List<Engine> getEngines() {
        return getRecipeInstance().getEngines();
    }

    @Override
    public List<Module> getModules() {
        return getRecipeInstance().getModules();
    }

    @Override
    public List<Weapon> getWeapons() {
        return getRecipeInstance().getWeapons();
    }
}
