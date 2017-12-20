package com.middlewar.core.model.space;

import com.middlewar.core.model.Base;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @OneToMany(mappedBy = "planet", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Base> bases;

    public Planet(String name, AstralObject parent) {
        super(name, parent);
        setBases(new ArrayList<>());
    }

    public void addBase(Base base) {
        if (!getBases().contains(base)) getBases().add(base);
    }

    @Override
    public String toString() {
        return "Planet{" +
                "baseCount=" + bases.size() +
                "," + super.toString() +
                '}';
    }
}
