package com.middlewar.core.model.space;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author bertrand.
 */
@Entity
@NoArgsConstructor
public class BlackHole extends AstralObject {

    public BlackHole(String name) {
        super(name, null);
    }
}
