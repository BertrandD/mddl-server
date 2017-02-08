package com.middlewar.core.model.space;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.model.Base;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bertrand.
 */
public class Planet extends AstralObject {

    @DBRef
    @JsonBackReference
    private List<Base> bases;

    public Planet(String name, AstralObject parent) {
        super(name, parent);
        bases = new ArrayList<>();
    }

    public List<Base> getBases() {
        return bases;
    }

    public void setBases(List<Base> bases) {
        this.bases = bases;
    }
}
