package com.middlewar.core.model.space;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author bertrand.
 */
@NoArgsConstructor
@Entity
public class Star extends AstralObject {
    public Star(String name, AstralObject parent) {
        super(name, parent);
    }
}
