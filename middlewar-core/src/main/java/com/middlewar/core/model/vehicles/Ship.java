package com.middlewar.core.model.vehicles;

import com.middlewar.core.model.Base;
import com.middlewar.core.model.instances.RecipeInstance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
@Entity
@Table(name = "ships")
@NoArgsConstructor
public class Ship extends Vehicle {
    public Ship(Base base, RecipeInstance recipeInstance, long count) {
        super(base, recipeInstance, count);
    }
}
