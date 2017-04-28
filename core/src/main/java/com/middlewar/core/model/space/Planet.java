package com.middlewar.core.model.space;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.model.Base;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bertrand.
 */
@Data
public class Planet extends AstralObject {

    @DBRef
    @JsonBackReference
    private List<Base> bases;

    public Planet(String name, AstralObject parent) {
        super(name, parent);
        setBases(new ArrayList<>());
    }

    public void addBase(Base base) {
        if(!getBases().contains(base)) getBases().add(base);
    }
}
