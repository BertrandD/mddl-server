package com.middlewar.core.model.vehicles;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import com.middlewar.core.serializer.ShipSerializer;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author LEBOC Philippe
 */
@Data
@Entity
@JsonSerialize(using = ShipSerializer.class)
public class Ship extends Vehicle {
    public Ship(Base base, RecipeInstance recipeInstance, long count) {
        super(base, recipeInstance, count);
    }
}
