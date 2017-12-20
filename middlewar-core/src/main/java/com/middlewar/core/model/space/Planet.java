package com.middlewar.core.model.space;

import com.middlewar.core.model.Base;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bertrand.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Planet extends AstralObject {

    @OneToMany(mappedBy = "planet", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Base> bases;

    public Planet(String name, AstralObject parent) {
        super(name, parent);
        setBases(new ArrayList<>());
    }

    public void addBase(Base base) {
        if (!getBases().contains(base)) getBases().add(base);
    }
}
