package com.middlewar.core.model.space;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author bertrand.
 */
@NoArgsConstructor
@Entity
public class Moon extends AstralObject {
    public Moon(String name, AstralObject parent) {
        super(name, parent);
    }
}
