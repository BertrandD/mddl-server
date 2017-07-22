package com.middlewar.core.model.space;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bertrand.
 */
@Data
@NoArgsConstructor
@Entity
public class Planet extends AstralObject {

    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL, orphanRemoval = true)
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
