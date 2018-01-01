package com.middlewar.core.model.buildings;

import com.middlewar.core.model.commons.StatsSet;
import com.middlewar.core.model.items.Module;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LEBOC Philippe
 */
@Getter
@Setter
public class ModulableBuilding extends Building {

    private List<Module> authorizedModules;
    private int maxModules;

    public ModulableBuilding(StatsSet set) {
        super(set);
        setAuthorizedModules(new ArrayList<>());
        setMaxModules(set.getInt("max_modules", 1));
    }

    @Override
    public String toString() {
        return "ModulableBuilding{" +
                "authorizedModules=" + authorizedModules +
                ", maxModules=" + maxModules +
                ", " + super.toString() +
                '}';
    }
}
