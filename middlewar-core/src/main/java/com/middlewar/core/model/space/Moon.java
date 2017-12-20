package com.middlewar.core.model.space;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author bertrand.
 */
@Entity
@NoArgsConstructor
public class Moon extends AstralObject {
    public Moon(String name, AstralObject parent) {
        super(name, parent);
    }
}
