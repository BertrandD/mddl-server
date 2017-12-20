package com.middlewar.core.model.vehicles;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
public class Ship extends Vehicle {
    public Ship(Base base, RecipeInstance recipeInstance, long count) {
        super(base, recipeInstance, count);
    }
}
